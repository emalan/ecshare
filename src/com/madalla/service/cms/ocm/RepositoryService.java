package com.madalla.service.cms.ocm;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.ocm.query.Filter;
import org.apache.jackrabbit.ocm.query.Query;
import org.apache.jackrabbit.ocm.query.QueryManager;
import org.joda.time.DateTime;
import org.springmodules.jcr.JcrCallback;

import com.madalla.service.cms.AbstractBlog;
import com.madalla.service.cms.AbstractBlogEntry;
import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.jcr.Content;
import com.madalla.service.cms.ocm.RepositoryInfo.RepositoryType;
import com.madalla.service.cms.ocm.blog.Blog;
import com.madalla.service.cms.ocm.blog.BlogEntry;
import com.madalla.service.cms.ocm.image.Album;
import com.madalla.service.cms.ocm.image.Image;
import com.madalla.service.cms.ocm.image.ImageHelper;
import com.madalla.util.jcr.ParentNodeCallback;
import com.madalla.util.jcr.RepositoryTemplate;
import com.madalla.util.jcr.ocm.JcrOcmConversion;

/**
 * Content Service Implementation for Jackrabbit JCR Content Repository
 * <p>
 * This class is aware of the structure of the data in the repository 
 * and will create the structure if it does not exist. The schema is
 * open and not enforced by the repository. 
 * <p>
 * <pre>
 *            ec:apps 
 *         -----|------------------------------                 
 *        |                                    |
 *     [ec:site1]                          [ec:site2]                               
 *        |                                    |
 * 
 * </pre>
 * 
 * @author Eugene Malan
 *
 */
public class RepositoryService extends AbstractRepositoryService implements IRepositoryService, Serializable{

	private static final long serialVersionUID = 795763276139305054L;
	private static final Log log = LogFactory.getLog(RepositoryService.class);

    private com.madalla.service.cms.jcr.RepositoryService oldRepositoryService;
    private RepositoryTemplate repositoryTemplate;

    //Delete this and move out to Data classes
    static final String EC_PROP_CONTENT = "ec:" + "content";

    public void init(){
    	super.init();
    	repositoryTemplate = new RepositoryTemplate(template, ocm, site);
    	
    	//do Conversion
    	JcrOcmConversion conversion = new JcrOcmConversion();
    	conversion.init(template, oldRepositoryService, this,site);
    	conversion.convertNodesToOcm();
    	
    }
    public boolean isDeletableNode(final String path){
    	return RepositoryInfo.isDeletableNode(template, path);
    }
    
    public boolean isContentNode(final String path){
    	return oldRepositoryService.isContentNode(path);
    }
    
    public boolean isBlogNode(final String path){
    	return RepositoryInfo.isBlogNodeType(path);
    }
    
    public boolean isImageNode(final String path){
    	return RepositoryInfo.isImagesNodeType(path);
    }
    
    public boolean isContentPasteNode(final String path){
    	return oldRepositoryService.isContentPasteNode(path);
    }
    
    /**
     * Creates the album if it does not exist.
     * @param name - album name
     * @return - album
     */
    public Album getAlbum(final String name){
    	return (Album) repositoryTemplate.executeParent(RepositoryType.ALBUM, name, new ParentNodeCallback(){

			@Override
			public AbstractOcm createNew(String parentPath, String name) {
				return new Album();
			}

    	});
    }
    
    private static final String ORIGINAL_ALBUM_NAME = "Originals";
	
    /**
     * The originals album is a storage space for all uploaded images. The uploaded images can be copied out
     * to albums for display on the site. the album is created if it does not exist.
     * 
     * @return - the album where we store all uploaded images
     */
    public Album getOriginalsAlbum(){
		return (Album) repositoryTemplate.executeParent(RepositoryType.ALBUM,ORIGINAL_ALBUM_NAME , new ParentNodeCallback(){

			@Override
			public Object createNew(String parentPath, String name) {
				return new Album(parentPath, name);
			}
			
		});
	}

	public String createImage(Album album, String name, InputStream inputStream) {
        //TODO use repository Template
	    
	    //scale image down to defaults if necessary
	    inputStream = ImageHelper.scaleOriginalImage(inputStream);
		
		Image image = new Image(album, name, inputStream);
        if (ocm.objectExists(image.getId())){
            ocm.update(image);
	    } else {
	        ocm.insert(image);
	    }
	    ocm.save();
	    
	    //post save Thumbnail creation
	    Image postProcessing = (Image) ocm.getObject(Image.class, image.getId());
	    postProcessing.setImageThumb(ImageHelper.scaleThumbnailImage(postProcessing.getImageFull()));
	    ocm.update(postProcessing);
	    ocm.save();
	    return postProcessing.getId();
	}
	
	public String addImageToAlbum(Album album, String imageId) {
	    Image original = (Image) ocm.getObject(Image.class,imageId);

	    InputStream inputStream = ImageHelper.scaleAlbumImage(original.getImageFull());
	    Image albumImage = new Image(album, original.getName(), inputStream);
	    albumImage.setImageThumb(original.getImageThumb());
	    ocm.insert(albumImage);
	    ocm.save();
		return albumImage.getId();
	}
	
    public Image getImage(final String path) {
        if (StringUtils.isEmpty(path)){
            log.error("getImage - path is required.");
            return null;
        }
        return (Image) ocm.getObject(Image.class, path);
    }
    
    public List<Image> getAlbumImages(Album album){
        //TODO create a query template
        QueryManager queryManager = ocm.getQueryManager();
        Filter filter = queryManager.createFilter(Image.class);
        filter.setScope(album.getId()+"//");
        Query query = queryManager.createQuery(filter);
        Collection collection =  ocm.getObjects(query);
        List<Image> list = new ArrayList<Image>();
        for(Iterator iter = collection.iterator(); iter.hasNext();){
            list.add((Image) iter.next());
        }
        Collections.sort(list);
        return list;
    }
    
    public TreeModel getAlbumImagesAsTree(final Album album) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(album.getName());
        TreeModel model = new DefaultTreeModel(rootNode);
        for(Image image: getAlbumImages(album)){
            DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(image);
            rootNode.add(treeNode);
        }
        return model;
    }
    
	public List<Image> getAlbumOriginalImages() {
	    Album album = getOriginalsAlbum();
	    return getAlbumImages(album);
	}
	
	public Blog getBlog(final String blogName){
		return (Blog) repositoryTemplate.executeParent(RepositoryType.BLOG, blogName, new ParentNodeCallback(){

			@Override
			public Object createNew(String parentPath, String name) {
				return new Blog(parentPath, name);
			}
			
		});
	}

	public BlogEntry getNewBlogEntry(AbstractBlog blog, String title, DateTime date){
    	return new BlogEntry(blog, title, date );
    }

    public BlogEntry getBlogEntry(final String path) {
        //TODO use repository Template
        if (StringUtils.isEmpty(path)){
            log.error("getBlogEntry - path is required.");
            return null;
        }
        return (BlogEntry) ocm.getObject(BlogEntry.class, path);
    }
    
    public void saveBlogEntry(BlogEntry blogEntry){
    	if (ocm.objectExists(blogEntry.getId())){
    		ocm.update(blogEntry);
    	} else {
    		ocm.insert(blogEntry);
    	}
    	ocm.save();
    }
    
    @Deprecated
    public AbstractBlogEntry getNewBlogEntry(String blog, String title, DateTime date){
    	return null;
    }
    
    public List<AbstractBlogEntry> getBlogEntries(AbstractBlog blog){
		QueryManager queryManager = ocm.getQueryManager();
		Filter filter = queryManager.createFilter(BlogEntry.class);
		filter.setScope(blog.getId()+"//");
		Query query = queryManager.createQuery(filter);
		Collection collection =  ocm.getObjects(query);
		List<AbstractBlogEntry> list = new ArrayList<AbstractBlogEntry>();
		for(Iterator iter = collection.iterator(); iter.hasNext();){
			list.add((AbstractBlogEntry) iter.next());
		}
		Collections.sort(list);
		return list;
    }
    
    public String getContentData(final String nodeName, final String id, Locale locale) {
        String localeId = getLocaleId(id, locale);
        return getContentData(nodeName, localeId);
    }
    
    public String getContentData(final String page, final String contentId) {
    	//return ContentHelper.getInstance().getData(page, contentId);
    	return oldRepositoryService.getContentData(page, contentId);
    }
    
    public String getLocaleId(String id, Locale locale) {
        Locale found = null;
        for (Iterator<Locale> iter = locales.iterator(); iter.hasNext();) {
            Locale current = iter.next();
            if (current.getLanguage().equals(locale.getLanguage())){
                found = current;
                break;
            }
        }
        if (null == found){
            return id;
        } else {
            return id + "_"+ found.getLanguage();
        }
    }

    public void deleteNode(final String path) {
        if (StringUtils.isEmpty(path)) {
            log.error("deleteNode - path is required.");
        } else {
            template.execute(new JcrCallback() {
                public Object doInJcr(Session session) throws IOException, RepositoryException {
                    Node node = (Node) session.getItem(path);
                    node.remove();
                    session.save();
                    return null;
                }
            });
        }
    }
    
    public List<AbstractBlogEntry> getBlogEntries(final String blog){
    	//return BlogEntryHelper.getInstance().getBlogEntries(blog);
    	return oldRepositoryService.getBlogEntries(blog);
    }

	public Content getContent(final String path) {
        return (Content) template.execute(new JcrCallback(){
            public Content doInJcr(Session session) throws IOException, RepositoryException {
                Node node = (Node) session.getItem(path);
                String pageName = node.getParent().getName().replaceFirst("ec:","");
                Content content = new Content(node.getPath(), pageName, node.getName());
                content.setText(node.getProperty(EC_PROP_CONTENT).getString());
                return content;
            }
        });
	}
    
    public void pasteContent(final String path, final Content content){
        log.debug("pasteContent - path="+path);
    	template.execute(new JcrCallback(){
			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node parent = (Node) session.getItem(path);
                Node newNode = getCreateNode(content.getName(), parent);
                newNode.setProperty(EC_PROP_CONTENT, content.getText());
                session.save();
                log.debug("pasteContent - Done pasting. path="+path);
                return null;
			}
    	});
    }

	public com.madalla.service.cms.jcr.RepositoryService getOldRepositoryService() {
		return oldRepositoryService;
	}

	public void setOldRepositoryService(com.madalla.service.cms.jcr.RepositoryService oldRepositoryService) {
		this.oldRepositoryService = oldRepositoryService;
	}
    
    

}
