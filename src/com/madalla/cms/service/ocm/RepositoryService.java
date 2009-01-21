package com.madalla.cms.service.ocm;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

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

import com.madalla.bo.AbstractData;
import com.madalla.bo.AlbumData;
import com.madalla.bo.BlogData;
import com.madalla.bo.BlogEntryData;
import com.madalla.bo.ContentData;
import com.madalla.bo.IAlbumData;
import com.madalla.bo.IBlogData;
import com.madalla.bo.IImageData;
import com.madalla.bo.IPageData;
import com.madalla.bo.ImageData;
import com.madalla.bo.PageData;
import com.madalla.cms.bo.impl.ocm.blog.Blog;
import com.madalla.cms.bo.impl.ocm.blog.BlogEntry;
import com.madalla.cms.bo.impl.ocm.image.Album;
import com.madalla.cms.bo.impl.ocm.image.Image;
import com.madalla.cms.bo.impl.ocm.image.ImageHelper;
import com.madalla.cms.bo.impl.ocm.page.Content;
import com.madalla.cms.bo.impl.ocm.page.Page;
import com.madalla.cms.bo.impl.ocm.security.User;
import com.madalla.cms.jcr.JcrUtils;
import com.madalla.cms.service.ocm.RepositoryInfo.RepositoryType;
import com.madalla.cms.service.ocm.template.ParentNodeCallback;
import com.madalla.cms.service.ocm.template.RepositoryTemplate;
import com.madalla.service.IRepositoryService;

/**
 * Content Service Implementation for Jackrabbit JCR Content Repository
 * using Jackrabbit OCM framework
 * <p>
 * This implentation uses Jackrabbit OCM to persist most of the Bean 
 * data. The schema of the Repository is stored in the RepositoryInfo class  
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
 * @see com.madalla.cms.service.ocm.madalla.service.cms.ocm.RepositoryInfo
 *
 */
public class RepositoryService extends AbstractRepositoryService implements IRepositoryService, Serializable{

	private static final long serialVersionUID = 795763276139305054L;
	private static final Log log = LogFactory.getLog(RepositoryService.class);

    private RepositoryTemplate repositoryTemplate;

    public void init(){
    	super.init();
    	repositoryTemplate = new RepositoryTemplate(template, ocm, site);
    }

    public boolean isDeletableNode(final String path){
    	return RepositoryInfo.isDeletableNode(template, path);
    }
    
    public boolean isContentNode(final String path){
    	return RepositoryInfo.isContentNodeType(path);
    }
    
    public boolean isBlogNode(final String path){
    	return RepositoryInfo.isBlogNodeType(path);
    }
    
    public boolean isImageNode(final String path){
    	return RepositoryInfo.isImagesNodeType(path);
    }
    
    public boolean isContentPasteNode(final String path){
    	return RepositoryInfo.isContentPasteNode(template, path);
    }
    
    public void deleteNode(final String path) {
    	JcrUtils.deleteNode(template, path);
    }
    
    //*************************
    // *** Album and Images ***
    private static final String ORIGINAL_ALBUM_NAME = "Originals";
    
    /**
     * Creates the album if it does not exist.
     * @param name - album name
     * @return - album
     */
    public Album getAlbum(final String name){
    	return (Album) repositoryTemplate.executeParent(RepositoryType.ALBUM, name, new ParentNodeCallback(){

			@Override
			public AbstractData createNew(String parentPath, String name) {
				return new Album(parentPath, name);
			}

    	});
    }
	
    /**
     * The originals album is a storage space for all uploaded images. The uploaded images can be copied out
     * to albums for display on the site. the album is created if it does not exist.
     * 
     * @return - the album where we store all uploaded images
     */
    public AlbumData getOriginalsAlbum(){
		return (Album) repositoryTemplate.executeParent(RepositoryType.ALBUM,ORIGINAL_ALBUM_NAME , new ParentNodeCallback(){

			@Override
			public AbstractData createNew(String parentPath, String name) {
				return new Album(parentPath, name);
			}
			
		});
	}

	public String createImage(IAlbumData album, String name, InputStream inputStream) {
	    //scale image down to defaults if necessary
		Image image = new Image(album, name, inputStream);
		saveDataObject(image);

	    ImageHelper.saveImageFull(template, image.getId(), inputStream);
	    ImageHelper.saveImageThumb(template, image.getId());
	    return image.getId();
	}
	
	public void addImageToAlbum(IAlbumData album, String imageName) {
	    IAlbumData orginalAlbum = getOriginalsAlbum();
	    Image original = (Image) ocm.getObject(Image.class, orginalAlbum.getId() + "/" + imageName);
	    
	    String path = album.getId() + "/" + original.getName();
	    ocm.copy(original.getId(), path);
	    ocm.save();
	    ImageHelper.resizeAlbumImage(template, path);
	}
	
    public IImageData getImage(final String path) {
        if (StringUtils.isEmpty(path)){
            log.error("getImage - path is required.");
            return null;
        }
        return (IImageData) ocm.getObject(Image.class, path);
    }
    
    public List<ImageData> getAlbumImages(AlbumData album){
        Collection collection =  getQueryData(Image.class, album);
        List<ImageData> list = new ArrayList<ImageData>();
        for(Iterator iter = collection.iterator(); iter.hasNext();){
            list.add((Image) iter.next());
        }
        Collections.sort(list);
        return list;
    }

    public TreeModel getAlbumImagesAsTree(final AlbumData album) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(album.getName());
        TreeModel model = new DefaultTreeModel(rootNode);
        for(ImageData image: getAlbumImages(album)){
            DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(image);
            rootNode.add(treeNode);
        }
        return model;
    }
    
	public List<ImageData> getAlbumOriginalImages() {
	    AlbumData album = getOriginalsAlbum();
	    return getAlbumImages(album);
	}

    //*****************************
    // *** Blog and BlogEntries ***
	
	public Blog getBlog(final String blogName){
		return (Blog) repositoryTemplate.executeParent(RepositoryType.BLOG, blogName, new ParentNodeCallback(){

			@Override
			public AbstractData createNew(String parentPath, String name) {
				return new Blog(parentPath, name);
			}
			
		});
	}

	public BlogEntry getNewBlogEntry(IBlogData blog, String title, DateTime date){
    	return new BlogEntry(blog, title, date );
    }

    public BlogEntry getBlogEntry(final String path) {
        if (StringUtils.isEmpty(path)){
            log.error("getBlogEntry - path is required.");
            return null;
        }
        return (BlogEntry) ocm.getObject(BlogEntry.class, path);
    }
    
    public void saveBlogEntry(BlogEntryData blogEntry){
    	saveDataObject(blogEntry);
    }
    
    public List<BlogEntryData> getBlogEntries(BlogData blog){
		Collection collection =  getQueryData(BlogEntry.class, blog);
		List<BlogEntryData> list = new ArrayList<BlogEntryData>();
		for(Iterator iter = collection.iterator(); iter.hasNext();){
			list.add((BlogEntryData) iter.next());
		}
		Collections.sort(list);
		return list;
    }
    
    //*************************
    // *** Page and Content ***
    

    /* (non-Javadoc)
     * @see com.madalla.service.cms.IRepositoryService#getPage(java.lang.String)
     */
    public PageData getPage(final String name){
        return (PageData) repositoryTemplate.executeParent(RepositoryType.PAGE, name, new ParentNodeCallback(){

            @Override
            public AbstractData createNew(String parentPath, String name) {
                return new Page(parentPath, name);
            }

        });
    }
    
    public String getContentText(final IPageData page, final String id, Locale locale) {
        String localeId = getLocaleId(id, locale);
        return getContentText(page, localeId);
    }
    
    public String getContentText(final IPageData page, final String contentName) {
        String path = page.getId() + "/" + contentName;
        ContentData content;
        if (ocm.objectExists(path)){
            content = (Content) ocm.getObject(Content.class, page.getId() + "/" + contentName);
        } else {
            content = new Content(page, contentName);
            content.setText("New Content");
            ocm.insert(content);
            ocm.save();
        }
        
        return content.getText();
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

    public void saveContent(ContentData content){
    	saveDataObject(content);
    }
    
    public Content getContent(final IPageData parent, final String name, final Locale locale){
    	return getContent(parent, getLocaleId(name, locale));
    }
    
    public Content getContent(final IPageData parent, final String name){
    	return getContent(parent.getId() + "/" + name);
    }

    public Content getContent(final String id) {
		return (Content) ocm.getObject(Content.class, id);
	}
    
    public void pasteContent(final String path, final ContentData content){
        copyData(path, content);
    }

    //*************************
    // ******   Users    ******
    
     
    public User getNewUser(String username, String password){
    	if (isUserExists(username)){
    		return null;
    	}
    	return getUser(username, password);
    }
    
    public User getUser(String username, final String password){
    	return (User) repositoryTemplate.executeParent(RepositoryType.USER, username, new ParentNodeCallback(){

			@Override
			public AbstractData createNew(String parentPath, String name) {
				User user = new User(parentPath, name);
				user.setPassword(password);
				return user;
			}

    	});
    }
    
    private boolean isUserExists(String username){
    	return repositoryTemplate.checkExists(RepositoryType.USER, username);
    }

    //************************************
    // *****  Utility methods
    
    private void saveDataObject(AbstractData data){
    	if (ocm.objectExists(data.getId())){
    		ocm.update(data);
    	} else {
    		ocm.insert(data);
    	}
    	ocm.save();
    }
    
    private Collection getQueryData(Class data, AbstractData parent){
        QueryManager queryManager = ocm.getQueryManager();
        Filter filter = queryManager.createFilter(data);
        filter.setScope(parent.getId()+"//");
        Query query = queryManager.createQuery(filter);
        return  ocm.getObjects(query);
    }
    
    private void copyData(final String path, final AbstractData data){
        String destPath = path+ "/" + data.getName();
        if (ocm.objectExists(destPath)){
            ocm.remove(destPath);
            ocm.save();
        }
        ocm.copy(data.getId(), destPath);
        ocm.save();
    }


}
