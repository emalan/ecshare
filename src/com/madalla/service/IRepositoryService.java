package com.madalla.service;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import javax.swing.tree.TreeModel;

import org.joda.time.DateTime;

import com.madalla.bo.SiteData;
import com.madalla.bo.blog.BlogData;
import com.madalla.bo.blog.BlogEntryData;
import com.madalla.bo.blog.IBlogData;
import com.madalla.bo.image.AlbumData;
import com.madalla.bo.image.IAlbumData;
import com.madalla.bo.image.IImageData;
import com.madalla.bo.image.ImageData;
import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.IPageData;
import com.madalla.bo.page.PageData;
import com.madalla.bo.page.ResourceData;
import com.madalla.bo.security.UserData;
import com.madalla.bo.security.UserSiteData;
import com.madalla.webapp.security.IAuthenticator;

/**
 * Main API for accessing Data Objects from the Content Repository.
 * <p>
 * Access to Business Objects that are persisted in the CMS. Methods to get
 * and save the objects and to check what type a Node is.
 * - Page and Content
 * - Blog and Blog entries
 * - Album and Images 
 * </p>
 * 
 * @author Eugene Malan
 *
 */
public interface IRepositoryService {

	String getLocaleId(String id, Locale locale);

	// type checks
	boolean isDeletableNode(final String id);

	boolean isContentNode(final String id);

	boolean isBlogNode(final String id);

	boolean isImageNode(final String id);

	boolean isContentPasteNode(final String id);

	// Generic
	void deleteNode(final String id);

	// Content
	PageData getPage(final String name);

	String getContentText(IPageData page, String id);

	String getContentText(IPageData page, String id, Locale locale);

	ContentData getContent(final IPageData parent, final String name,
			final Locale locale);

	ContentData getContent(final IPageData parent, final String name);

	ContentData getContent(final String id);

	void saveContent(ContentData content);

	void pasteContent(String id, ContentData content);

    void createContentResource(final IPageData page, final String name, final InputStream inputStream );
    
    ResourceData getContentResource(final IPageData page, final String name);

	// Blog
	BlogData getBlog(final String blogName);

	BlogEntryData getNewBlogEntry(IBlogData blog, String title, DateTime date);

	BlogEntryData getBlogEntry(final String id);

	void saveBlogEntry(BlogEntryData blogEntry);

	List<BlogEntryData> getBlogEntries(BlogData blog);

	// Image
	AlbumData getOriginalsAlbum();

	AlbumData getAlbum(final String name);

	String createImage(IAlbumData album, String name, InputStream inputStream);

	void addImageToAlbum(IAlbumData album, String name);

	IImageData getImage(final String id);

	List<ImageData> getAlbumImages(AlbumData album);

	List<ImageData> getAlbumOriginalImages();

	TreeModel getAlbumImagesAsTree(final AlbumData album);
	
	//User
	SiteData getSite(String name);
	
	List<SiteData> getSiteEntries();
	
	void saveSite(SiteData data);
	
    UserData getNewUser(String username, String password);
    
    UserData getUser(String username);
    
    void saveUser(UserData user);
    
    IAuthenticator getUserAuthenticator();
    
    List<UserData> getUsers();
    
    List<UserSiteData> getUserSiteEntries(UserData user);
    
    void saveUserSiteEntries(UserData user, List<SiteData> sites);
    
}
