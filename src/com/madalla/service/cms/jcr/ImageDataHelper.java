package com.madalla.service.cms.jcr;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.image.ImageUtilities;
import com.madalla.image.LoggingImageObserver;
import com.madalla.service.cms.AbstractImageData;
import com.madalla.service.cms.IRepositoryData;

/**
 * Handles Image Data persistance to a JCR Content Repository
 * <p>
 * Responsible for getting data into and from a JCR Repository. This class controls the data
 * structure/schema in the Repository.
 * </p>
 * <p>
 * There is a generic structure for all types of data and for that schema albums map to groups.
 * ec:albums ---> group
 * ec:original ---> group (special group for originals)
 * </p>
 * <pre>
 *     [ec:site]  ----- ec:images 
 *                  -------|----------------                 
 *                 |                        |
 *             ec:originals            [ec:albums]  
 * </pre>
 * 
 * 
 * @author Eugene Malan
 *
 */
class ImageDataHelper extends AbstractContentHelper {
	
	// Repository Values
    static final String EC_NODE_IMAGES = NS + "images";
    private static final String EC_ORIGINALS = "originals";
    static final String EC_PROP_TITLE = NS + "title";
    private static final String EC_IMAGE_FULL = "imageFull";
    private static final String EC_IMAGE_THUMB = "imageThumb";
    static final String EC_PROP_DESCRIPTION = NS + "description";
    
    private static final int MAX_WIDTH = 900;
    private static final int MAX_HEIGHT = 600;
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 266;
    private static final int THUMB_WIDTH = 90;
    private static final int THUMB_HEIGHT = 60;
    
    private static final Log log = LogFactory.getLog(ImageDataHelper.class);
	private static ImageDataHelper instance;
	public static ImageDataHelper getInstance(){
		return instance;
	}

    private static AbstractImageData create(Node node) throws RepositoryException{
    	String parent = node.getParent().getName().replaceFirst(NS,"");
    	String name = node.getName().replaceFirst(NS, "");
    	InputStream image = node.getProperty(EC_IMAGE_FULL).getStream();
    	ImageData data = new ImageData(node.getPath(), parent, name, createImageResource(image));
    	data.setDescription(node.getProperty(EC_PROP_DESCRIPTION).getString());
    	data.setTitle(node.getProperty(EC_PROP_TITLE).getString());
    	if (node.hasProperty(EC_IMAGE_THUMB)){
    		InputStream thumb = node.getProperty(EC_IMAGE_THUMB).getStream();
    		data.setThumbnail(createImageResource(thumb));
    	}
    	data.setUrl("http://www.emalan.org");
    	return data;
    }
    
	static DynamicImageResource createImageResource(InputStream inputStream){
		BufferedDynamicImageResource webResource = null;
		try {
			webResource = new BufferedDynamicImageResource();
			webResource.setImage(ImageIO.read(inputStream));

		} catch (Exception e) {
			log.error("Exception while reading image from Content.",e);
			throw new WicketRuntimeException("Error while reading image from Content Management System.");
		}
		return webResource;
	}
	



	public ImageDataHelper(String site, JcrTemplate template ){
		this.site = site;
		this.template = template;
		instance = this;
	}

	String saveOriginalImage(final String name, final InputStream fullImage ){
        ImageData imageData = new ImageData(EC_ORIGINALS, name, fullImage);
        String path = save(imageData);
        createThumbnail(path);
        return path;
    }
	
    String saveAlbumImage(final String album, final String name){
    	if (StringUtils.isEmpty(name)){
    		throw new WicketRuntimeException("Album Image Name needs to be supplied.");
    	}
    	return (String)template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException, RepositoryException {
				Node siteNode = getSiteNode(session);
	        	Node parent = getParentNode(siteNode);
	        	Node groupNode = getCreateNode(NS+EC_ORIGINALS, parent);
				Node node = (Node) groupNode.getNode(NS+name);
				InputStream fullImage = node.getProperty(EC_IMAGE_FULL).getStream();
				InputStream albumImage = scaleAlbumImage(fullImage);
				ImageData imageData = new ImageData(album, name, albumImage);
				return save(imageData);
			}
		});
    }
    
	
	String save(final ImageData imageData) {
		return genericSave(imageData);
    }
	
	ImageData get(final String album, final String name){
		return (ImageData) template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,	RepositoryException {
				Node siteNode = getSiteNode(session);
	        	Node parent = getParentNode(siteNode);
	        	Node groupNode = getCreateNode(NS+album, parent);
				Node node = (Node) groupNode.getNode(NS+name);
				return create(node);
			}
			
		});
	}
	
	ImageData get(final String path){
		return (ImageData) template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,	RepositoryException {
				Node node = (Node) session.getItem(path);
				return create(node);
			}
			
		});
	}
	
	TreeModel getAlbumEntriesAsTree(final String group){
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(group);
        TreeModel model = new DefaultTreeModel(rootNode);
        for(AbstractImageData imageData: getAlbumEntries(group)){
        	DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(imageData);
        	rootNode.add(treeNode);
        }
        return model;
	}
	
	List<AbstractImageData> getOriginalEntries(){
		return getEntries(NS + EC_ORIGINALS);
	}
	
	List<AbstractImageData> getAlbumEntries(final String group){
		return getEntries(NS+group);
	}
	
	@SuppressWarnings("unchecked")
	private List<AbstractImageData> getEntries(final String group){
		return (List<AbstractImageData>) template.execute(new JcrCallback(){
            List<AbstractImageData> list = new ArrayList<AbstractImageData>();
            public List<AbstractImageData> doInJcr(Session session) throws IOException, RepositoryException {
            	Node siteNode = getSiteNode(session);
            	Node parentNode = getParentNode(siteNode);
            	Node groupNode  = getCreateNode(group, parentNode);
                
                for (NodeIterator iterator = groupNode.getNodes(); iterator.hasNext();){
                    Node nextNode = iterator.nextNode();
                    try {
                    	list.add(create(nextNode));
                    } catch (RuntimeException e){
                    	log.error("Unable to create entry.",e);
                    }
                }
                return list;
            }
        });
	}
	
	private void createThumbnail(final String path){
		template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node node = (Node) session.getItem(path);
				InputStream fullImage = node.getProperty(EC_IMAGE_FULL).getStream();
				InputStream thumb = scaleThumbnailImage(fullImage);
				node.setProperty(EC_IMAGE_THUMB, thumb);
				session.save();
				return null;
			}
		});
	}
	
	private InputStream scaleOriginalImage(InputStream inputStream){
		LoggingImageObserver observer = new LoggingImageObserver(log);
		return ImageUtilities.scaleImageDownProportionately(inputStream, observer, MAX_WIDTH, MAX_HEIGHT);
	}
	
	private InputStream scaleThumbnailImage(InputStream inputStream){
		LoggingImageObserver observer = new LoggingImageObserver(log);
		return ImageUtilities.scaleImageDownProportionately(inputStream, observer, THUMB_WIDTH, THUMB_HEIGHT);
	}
	
	private InputStream scaleAlbumImage(InputStream inputStream){
		LoggingImageObserver observer = new LoggingImageObserver(log);
		return ImageUtilities.scaleImageDownProportionately(inputStream, observer, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
    
	@Override
    Node getParentNode(Node node) throws RepositoryException{
    	return getCreateNode(EC_NODE_IMAGES, node);
    }

	@Override
	void setPropertyValues(Node node, IRepositoryData data)
			throws RepositoryException {
		log.debug("setPropertyValue starting - "+data);
		ImageData imageData = (ImageData) data;
		node.setProperty(EC_PROP_TITLE, imageData.getTitle());
		node.setProperty(EC_PROP_DESCRIPTION, imageData.getDescription());
		node.setProperty(EC_IMAGE_FULL, scaleOriginalImage(imageData.getFullImageAsInputStream()));
		log.debug("setPropertyValue done - "+imageData);
	}
	

}
