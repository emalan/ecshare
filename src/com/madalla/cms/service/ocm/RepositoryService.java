package com.madalla.cms.service.ocm;

import java.io.InputStream;
import java.io.Serializable;
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
import org.joda.time.DateTime;

import com.madalla.bo.AbstractData;
import com.madalla.bo.SiteData;
import com.madalla.bo.blog.BlogData;
import com.madalla.bo.blog.BlogEntryData;
import com.madalla.bo.blog.IBlogData;
import com.madalla.bo.image.AlbumData;
import com.madalla.bo.image.IAlbumData;
import com.madalla.bo.image.IImageData;
import com.madalla.bo.image.ImageData;
import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.PageData;
import com.madalla.bo.page.ResourceData;
import com.madalla.bo.security.UserData;
import com.madalla.bo.security.UserSiteData;
import com.madalla.cms.bo.impl.ocm.Site;
import com.madalla.cms.bo.impl.ocm.blog.Blog;
import com.madalla.cms.bo.impl.ocm.blog.BlogEntry;
import com.madalla.cms.bo.impl.ocm.image.Album;
import com.madalla.cms.bo.impl.ocm.image.Image;
import com.madalla.cms.bo.impl.ocm.image.ImageHelper;
import com.madalla.cms.bo.impl.ocm.page.Content;
import com.madalla.cms.bo.impl.ocm.page.Page;
import com.madalla.cms.bo.impl.ocm.page.Resource;
import com.madalla.cms.bo.impl.ocm.security.User;
import com.madalla.cms.bo.impl.ocm.security.UserSite;
import com.madalla.cms.jcr.JcrUtils;
import com.madalla.cms.service.ocm.RepositoryInfo.RepositoryType;
import com.madalla.cms.service.ocm.template.RepositoryTemplate;
import com.madalla.cms.service.ocm.template.RepositoryTemplateCallback;
import com.madalla.service.IRepositoryService;
import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.security.IAuthenticator;

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
 * @see com.madalla.cms.service.ocm.RepositoryInfo
 *
 */
public class RepositoryService extends AbstractRepositoryService implements IRepositoryService, Serializable{

	private static final long serialVersionUID = 795763276139305054L;
	private static final Log log = LogFactory.getLog(RepositoryService.class);

    private RepositoryTemplate repositoryTemplate;

    public void init(){
    	super.init();
    	repositoryTemplate = new RepositoryTemplate(template, ocm, site);
    	
    	//Process data migration if necessary
    	RepositoryDataMigration.transformData(template, site);

    	//Create site node
    	getSite(site);
    	
    	//Create default Users if they don't exist yet
    	getNewUser("guest", SecurityUtils.encrypt("password"));
    	UserData adminUser = getNewUser("admin", SecurityUtils.encrypt("password"));
    	if (adminUser == null){
    	    adminUser = getUser("admin");
    	} else {
            saveUserSite(new UserSite(adminUser.getId(), site));
    	}
        adminUser.setAdmin(true);
        saveUser(adminUser);
    	
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
    
    public InputStream getResourceStream(String path, String property){
    	return repositoryTemplate.getNodePropertyStream(path, property);
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
    	return (Album) repositoryTemplate.getParentObject(RepositoryType.ALBUM, name, new RepositoryTemplateCallback(){

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
		return (Album) repositoryTemplate.getParentObject(RepositoryType.ALBUM,ORIGINAL_ALBUM_NAME , new RepositoryTemplateCallback(){

			@Override
			public AbstractData createNew(String parentPath, String name) {
				return new Album(parentPath, name);
			}
			
		});
	}

	public String createImage(IAlbumData album, String name, InputStream inputStream) {
	    //scale image down to defaults if necessary
		Image image = new Image(album, name);
		saveDataObject(image);
		
		//get the InputStream into repository
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
    
    
    @SuppressWarnings("unchecked")//TODO maybe we should create new List instead of Casting
	public List<ImageData> getAlbumImages(AlbumData album){
    	List<ImageData> list = (List<ImageData>) repositoryTemplate.getAll(RepositoryType.IMAGE, album);
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
		return (Blog) repositoryTemplate.getParentObject(RepositoryType.BLOG, blogName, new RepositoryTemplateCallback(){

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
    
	@SuppressWarnings("unchecked")//TODO Maybe better to create new List instead of the risky Cast
	public List<BlogEntryData> getBlogEntries(BlogData blog){
		List<BlogEntryData> list = (List<BlogEntryData>) repositoryTemplate.getAll(RepositoryType.BLOGENTRY, blog);
		Collections.sort(list);
		return list;
    }
    
    //*************************
    // *** Page and Content ***
    
    public PageData getPage(final String name){
        return (PageData) repositoryTemplate.getParentObject(RepositoryType.PAGE, name, new RepositoryTemplateCallback(){

            @Override
            public AbstractData createNew(String parentPath, String name) {
                return new Page(parentPath, name);
            }

        });
    }
    
    public String getContentText(final PageData page, final String id, Locale locale) {
        String localeId = getLocaleId(id, locale);
        return getContentText(page, localeId);
    }
    
    public String getContentText(final PageData page, final String contentName) {
    	return getContent(page, contentName).getText();
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
    
    public Content getContent(final PageData parent, final String name, final Locale locale){
    	return getContent(parent, getLocaleId(name, locale));
    }
    
    public Content getContent(final PageData page, final String contentName){
    	return (Content) repositoryTemplate.getOcmObject(RepositoryType.CONTENT, page, contentName, new RepositoryTemplateCallback(){

			@Override
			public AbstractData createNew(String parentPath, String name) {
				Content content = new Content(page, contentName);
	            content.setText("New Content");
				return content;
			}
    		
    	});
    }

    public Content getContent(final String id) {
		return (Content) ocm.getObject(Content.class, id);
	}
    
    public void pasteContent(final String path, final ContentData content){
        copyData(path, content);
    }
    
    //Resources
    public ResourceData getContentResource(final PageData page, final String name){
    	Resource data = (Resource) repositoryTemplate.getOcmObject(RepositoryType.RESOURCE, page, name, new RepositoryTemplateCallback(){

			@Override
			public AbstractData createNew(String parentPath, String name) {
				return new Resource(page, name);
			}
    		
    	});
    	return data;
    }
    
    
    
    public void saveContentResource(final ResourceData data){
    	saveDataObject(data);
    }

    //**********************************
    // ******   Site and Users    ******
    
    public SiteData getSite(String name){
    	return (SiteData) repositoryTemplate.getParentObject(RepositoryType.SITE, name, new RepositoryTemplateCallback(){
			
    		@Override
			public AbstractData createNew(String parentPath, String name) {
    			log.debug("createNew - creating Site :"+name);
    			return new Site(parentPath, name);
			}
    		
    	});
    }
    
    public void saveSite(SiteData data){
        saveDataObject(data);
    }
    
    @SuppressWarnings("unchecked")//another not-safe cast from Collection to List
	public List<SiteData> getSiteEntries(){
    	return (List<SiteData>) repositoryTemplate.getAll(RepositoryType.SITE);
    }
    
    public UserData getNewUser(String username, String password){
    	username = username.toLowerCase();
    	if (isUserExists(username)){
    		return null;
    	}
    	UserData user = getUser(username);
    	user.setPassword(password);
    	saveUser(user);
    	return user;
    }
    
    public UserData getUser(String username){
    	username = username.toLowerCase();
    	return (User) repositoryTemplate.getParentObject(RepositoryType.USER, username, new RepositoryTemplateCallback(){

			@Override
			public AbstractData createNew(String parentPath, String name) {
				return new User(parentPath, name);
			}

    	});
    }
    
    public void saveUser(UserData user){
        saveDataObject(user);
    }
    
    private boolean isUserExists(String username){
    	return repositoryTemplate.checkExists(RepositoryType.USER, username);
    }
    
	@SuppressWarnings("unchecked")
	public List<UserData> getUsers(){
		List<UserData> list = (List<UserData>) repositoryTemplate.getAll(RepositoryType.USER);
		Collections.sort(list);
		return list;
	}
	
	public IAuthenticator getUserAuthenticator() {
		return new IAuthenticator(){
			UserData userData = null;
			
			public boolean authenticate(String user, char[] password) {
				return authenticate(user, new String(password));
			}

			public boolean authenticate(String username, String password) {
				log.debug("authenticate - username="+username);
				if (isUserExists(username)){
					log.debug("authenticate - user found.");
					userData = getUser(username);
					log.debug("authenticate - password="+password);
					log.debug("authenticate - compare="+userData.getPassword());
					if (userData.getPassword().equals(password)){
						List<UserSiteData> sites = getUserSiteEntries(userData);
						for (UserSiteData siteData : sites){
							if (siteData.getName().equals(site)){
								return true;
							}
						}
						if (username.equals("admin") && site.equals("ecadmin")){
						    return true;
						}
					}
				}
				return false;
			}

		};
	}
	
	public void saveUserSite(UserSite data){
		saveDataObject(data);
	}
	
	@SuppressWarnings("unchecked") //Unsafe cast
	public List<UserSiteData> getUserSiteEntries(UserData user){
		List<UserSiteData> list = (List<UserSiteData>) repositoryTemplate.getAll(RepositoryType.USERSITE, user);
		Collections.sort(list);
		return list;
	}
	
	public void saveUserSiteEntries(UserData user, List<SiteData> sites){
		log.debug("saveUserSiteEntries -"+sites);
		for (UserSiteData userSite : getUserSiteEntries(user)){
			deleteNode(userSite.getId());
		}
		for(SiteData site : sites){
			saveUserSite(new UserSite(user.getId(), site.getName()));
		}
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
