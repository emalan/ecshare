package com.madalla.cms.service.ocm;

import java.io.Serializable;

import com.madalla.bo.AbstractData;
import com.madalla.bo.security.IUser;
import com.madalla.service.DataAccessException;
import com.madalla.service.ISessionDataService;


/**
 * Wrapper class that has session scope and adds security validation to the normal Repository
 * service
 * 
 * @author Eugene Malan
 *
 */
public class SessionDataService implements ISessionDataService, Serializable {
	
	private static final long serialVersionUID = 6043048251504290235L;
	
	private IUser user;
	
	public SessionDataService(){

	}
	
	public void validateTransaction(AbstractData data) throws DataAccessException{
		if (null == user){
			throw new DataAccessException("Access Denied! User needs to be set in Session for transaction to be authorized.");
		}
	}
	
	public IUser getUser() {
		return user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}

//	//Checks
//	public boolean isAdminApp() {
//		return service.isAdminApp();
//	}
//	public boolean isBlogNode(String id) {
//		return service.isBlogNode(id);
//	}
//	public boolean isContentNode(String id) {
//		return service.isContentNode(id);
//	}
//	public boolean isContentPasteNode(String id) {
//		return service.isContentNode(id);
//	}
//	public boolean isDeletableNode(String id) {
//		return service.isDeletableNode(id);
//	}
//	public boolean isImageNode(String id) {
//		return service.isImageNode(id);
//	}
//	
//	// *** Album and Images
//	public AlbumData getAlbum(String name) {
//		return service.getAlbum(name);
//	}
//	
//	public AlbumData getOriginalsAlbum() {
//		return service.getOriginalsAlbum();
//	}
//
//	public void addImageToAlbum(IAlbumData album, String name) {
//		service.addImageToAlbum(album, name);
//	}
//
//	public String createImage(IAlbumData album, String name, InputStream inputStream) {
//		return service.createImage(album, name, inputStream);
//	}
//
//	public List<ImageData> getAlbumImages(AlbumData album) {
//		return service.getAlbumImages(album);
//	}
//
//	public TreeModel getAlbumImagesAsTree(AlbumData album) {
//		return service.getAlbumImagesAsTree(album);
//	}
//
//	public List<ImageData> getAlbumOriginalImages() {
//		return service.getAlbumOriginalImages();
//	}
//	
//	//Site and users
//    public SiteData getSiteData() {
//        return service.getSiteData();
//    }
//	public SiteData getSite(String name) {
//		return service.getSite(name);
//	}
//	public List<SiteData> getSiteEntries() {
//		return service.getSiteEntries();
//	}
//    public UserData getUser(String username){
//    	return service.getUser(username);
//    }
//	public List<UserData> getUsers(){
//		return service.getUsers();
//	}
//	public IAuthenticator getUserAuthenticator() {
//		return service.getUserAuthenticator();
//	}
//
//
//	// *** Blog and Blog Entries
//	
//	public BlogData getBlog(String blogName) {
//		return service.getBlog(blogName);
//	}
//
//	public List<BlogEntryData> getBlogEntries(BlogData blog) {
//		return service.getBlogEntries(blog);
//	}
//
//	public BlogEntryData getBlogEntry(String id) {
//		return service.getBlogEntry(id);
//	}
//
//	public BlogEntryData getNewBlogEntry(IBlogData blog, String title, DateTime date) {
//		return service.getNewBlogEntry(blog, title, date);
//	}
//	
//
//
//	public void deleteNode(String id) {
//		service.deleteNode(id);
//	}
//
//
//	public ContentData getContent(PageData parent, String name) {
//		return service.getContent(parent, name);
//	}
//
//	public ContentData getContent(String id) {
//		return service.getContent(id);
//	}
//
//	public ResourceData getContentResource(PageData page, String name) {
//		return service.getContentResource(page, name);
//	}
//
//
//	public IImageData getImage(String id) {
//		return service.getImage(id);
//	}
//
//
//
//	public UserData getNewUser(String username, String password) {
//		return service.getNewUser(username, password);
//	}
//
//
//	public PageData getPage(String name) {
//		return service.getPage(name);
//	}
//
//	public InputStream getResourceStream(String path, String property) {
//		return service.getResourceStream(path, property);
//	}
//
//
//
//	public List<UserSiteData> getUserSiteEntries(UserData user) {
//		return service.getUserSiteEntries(user);
//	}
//
//
//
//	public void pasteContent(String id, ContentData content) {
//		service.pasteContent(id, content);
//		
//	}
//
//	public void saveBlogEntry(BlogEntryData blogEntry) {
//		service.saveBlogEntry(blogEntry);
//		
//	}
//
//	public void saveContent(ContentData content) {
//		service.saveContent(content);
//		
//	}
//
//	public void saveContentResource(ResourceData data) {
//		service.saveContentResource(data);
//		
//	}
//
//
//
//	public void saveUserSiteEntries(UserData user, List<SiteData> sites) {
//		service.saveUserSiteEntries(user, sites);
//		
//	}
//	public VideoPlayerData getVideoPlayerData(PageData page, String name) {
//		return service.getVideoPlayerData(page, name);
//	}
//	public void saveVideoPlayerData(VideoPlayerData data) {
//		service.saveVideoPlayerData(data);
//		
//	}
//	public ResourceData getContentResource(String id) {
//		return service.getContentResource(id);
//	}
//
//	public EmailData getEmail() {
//		return service.getEmail();
//	}
//	public List<EmailEntryData> getEmailEntries() {
//		return service.getEmailEntries();
//	}
//
//	public String createEmailEntry(EmailData parent, DateTime dateTime,
//			String name, String email, String comment) {
//		return service.createEmailEntry(parent, dateTime, name, email, comment);
//	}
//	public void deleteEmailEntry(String id) {
//		service.deleteEmailEntry(id);
//		
//	}
//	public EmailEntryData getEmailEntry(String id) {
//		return service.getEmailEntry(id);
//	}
//	public ContentEntryData getContentEntry(ContentData parent, Locale locale) {
//		return service.getContentEntry(parent, locale);
//	}
//	public ContentEntryData getInlineContentEntry(ContentData parent,
//			Locale locale) {
//		return service.getInlineContentEntry(parent, locale);
//	}
//	public void saveContentEntry(ContentEntryData data) {
//		service.saveContentEntry(data);		
//	}
//	public String getContentText(ContentData parent, Locale locale) {
//		return service.getContentText(parent, locale);
//	}

}
