package com.madalla.service;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import javax.swing.tree.TreeModel;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.madalla.bo.AbstractData;
import com.madalla.bo.SiteData;
import com.madalla.bo.blog.BlogData;
import com.madalla.bo.blog.BlogEntryData;
import com.madalla.bo.blog.IBlogData;
import com.madalla.bo.email.EmailEntryData;
import com.madalla.bo.image.AlbumData;
import com.madalla.bo.image.IAlbumData;
import com.madalla.bo.image.IImageData;
import com.madalla.bo.image.ImageData;
import com.madalla.bo.log.LogData;
import com.madalla.bo.member.MemberData;
import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.ContentEntryData;
import com.madalla.bo.page.PageData;
import com.madalla.bo.page.PageMetaLangData;
import com.madalla.bo.page.ResourceData;
import com.madalla.bo.security.IUser;
import com.madalla.bo.security.ProfileData;
import com.madalla.bo.security.UserData;
import com.madalla.bo.security.UserSiteData;
import com.madalla.bo.video.VideoPlayerData;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.webapp.security.IPasswordAuthenticator;

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
public interface IDataService {

	boolean isAdminApp();

	// type checks
	boolean isDeletableNode(final String id);

	boolean isContentNode(final String id);

	boolean isBlogNode(final String id);

	boolean isImageNode(final String id);

	// Generic
	void deleteNode(final String id);

	void saveDataObject(AbstractData data);

	void saveDataObject(AbstractData data, String user);

	InputStream getResourceStream(String path, String property);

	// Content
	PageData getPage(final String name);

	PageMetaLangData getPageMetaLang(final Locale locale, final PageData page);

	PageMetaLangData getPageMetaLang(final Locale locale, final PageData page, boolean display);

	String getContentText(final ContentData parent, Locale locale);

	ContentData getContent(final PageData parent, final String name);

	ContentData getContent(final String id);

    ContentEntryData getInlineContentEntry(final ContentData parent, final Locale locale);

    ContentEntryData getContentEntry(final ContentData parent, final Locale locale);

    ContentEntryData getContentEntry(final ContentData parent, String name, final String defaultText);

    ResourceData getContentResource(final PageData page, final String name);

    ResourceData getContentResource(final String id);

    void saveContentResource(final ResourceData data);

    VideoPlayerData getVideoPlayerData(final PageData page, final String name);

    void saveVideoPlayerData(final VideoPlayerData data);

	// Blog
	BlogData getBlog(final String blogName);

	BlogEntryData getNewBlogEntry(IBlogData blog, String title, DateTime date);

	BlogEntryData getBlogEntry(final String id);

	void saveBlogEntry(BlogEntryData blogEntry);

	List<BlogEntryData> getBlogEntries(BlogData blog);

	// Image
	List<AlbumData> getAlbums();

	AlbumData getOriginalsAlbum();

	AlbumData getAlbum(final String name);

	String createImage(IAlbumData album, String name, InputStream inputStream);

	void addImageToAlbum(IAlbumData album, String name);

	IImageData getImage(final String id);

	List<ImageData> getAlbumImages(AlbumData album);

	List<ImageData> getAlbumOriginalImages();

	TreeModel getAlbumImagesAsTree(final AlbumData album);

	// site data

	void createEmailEntry(String name, String email, String comment);

	EmailEntryData getEmailEntry(String id);

	void deleteEmailEntry(EmailEntryData email);

	List<EmailEntryData> getEmailEntries();

    LogData getTransactionLog(String id);

    List<LogData> getTransactionLogEntries();

	//Site
	DateTimeZone getDateTimeZone();

	SiteData getSite(String name);

	SiteData getSiteData();

	List<SiteData> getSiteEntries();

	//Member
    boolean isMemberExist(String name);

    boolean saveMember(MemberData member);

    IPasswordAuthenticator getMemberAuthenticator(String name);

    List<? extends MemberData> getMemberEntries();

    MemberData getMember(String memberId);

    MemberData getMemberById(String id);

    void deleteMember(MemberData data);

	//User
	ProfileData getProfile(String identifier);

	ProfileData getNewUserProfile(IUser user, String providerName, String identifier);

	UserData getUser(ProfileData profile);

    UserData getNewUser(String username, String password);

    UserData getUser(String username);

    IAuthenticator getUserAuthenticator();

    IPasswordAuthenticator getPasswordAuthenticator(String username);

    boolean isUserSite(UserData userData);

    List<UserData> getUsers();

    List<UserSiteData> getUserSiteEntries(UserData user);

    void saveUserSiteEntries(UserData user, List<SiteData> sites, boolean auth);

    UserSiteData getUserSite(UserData user);



}
