package com.madalla.cms.service.ocm;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.wicket.WicketRuntimeException;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.bo.AbstractData;
import com.madalla.bo.SiteData;
import com.madalla.bo.SiteLanguage;
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
import com.madalla.bo.page.PageMetaData;
import com.madalla.bo.page.PageMetaLangData;
import com.madalla.bo.page.ResourceData;
import com.madalla.bo.security.IUser;
import com.madalla.bo.security.ProfileData;
import com.madalla.bo.security.UserData;
import com.madalla.bo.security.UserSiteData;
import com.madalla.bo.video.VideoPlayerData;
import com.madalla.cms.bo.impl.ocm.Site;
import com.madalla.cms.bo.impl.ocm.blog.Blog;
import com.madalla.cms.bo.impl.ocm.blog.BlogEntry;
import com.madalla.cms.bo.impl.ocm.image.Album;
import com.madalla.cms.bo.impl.ocm.image.Image;
import com.madalla.cms.bo.impl.ocm.image.ImageHelper;
import com.madalla.cms.bo.impl.ocm.page.Content;
import com.madalla.cms.bo.impl.ocm.page.ContentEntry;
import com.madalla.cms.bo.impl.ocm.page.Page;
import com.madalla.cms.bo.impl.ocm.page.PageMeta;
import com.madalla.cms.bo.impl.ocm.page.PageMetaLang;
import com.madalla.cms.bo.impl.ocm.page.Resource;
import com.madalla.cms.bo.impl.ocm.video.VideoPlayer;
import com.madalla.cms.service.ocm.RepositoryInfo.RepositoryType;
import com.madalla.cms.service.ocm.template.RepositoryTemplate;
import com.madalla.cms.service.ocm.template.RepositoryTemplateCallback;
import com.madalla.cms.service.ocm.util.JcrOcmUtils;
import com.madalla.db.dao.EmailEntry;
import com.madalla.db.dao.EmailEntryDao;
import com.madalla.db.dao.TransactionLogDao;
import com.madalla.image.ImageUtilities;
import com.madalla.service.IDataService;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.webapp.security.IPasswordAuthenticator;

/**
 * Content Service Implementation for Jackrabbit JCR Content Repository
 * using Jackrabbit OCM framework
 * <p>
 * This implementation uses Jackrabbit OCM to persist most of the Bean
 * data. The schema of the Repository is stored in the RepositoryInfo class
 * </p>
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
public class RepositoryService extends AbstractRepositoryService implements IDataService, Serializable{

	private static final long serialVersionUID = 795763276139305054L;
	private static final Logger log = LoggerFactory.getLogger(RepositoryService.class);


    private EmailEntryDao emailEntryDao;
    private MemberService memberService;
    private UserSecurityService userSecurityService;

    public void init(){
       	Session session;
		try {
			session = template.getSessionFactory().getSession();
		} catch (RepositoryException e) {
			log.error("Exception while getting Session from JcrTemplate", e);
			throw new WicketRuntimeException("Exception getting Session from JcrTemplate", e);
		}
		ObjectContentManager ocm =  JcrOcmUtils.getObjectContentManager(session);

    	repositoryTemplate = new RepositoryTemplate(template, ocm, site);

    	//Process data migration if necessary
    	//RepositoryDataMigration.transformData(template, site);

    	ImageUtilities.validateImageIO();

    	userSecurityService.init(getSiteEntries(), repositoryTemplate);

    }

    public DateTimeZone getDateTimeZone(){
    	SiteData site = getSiteData();
    	DateTimeZone dateTimeZone;
    	try {
    		dateTimeZone = DateTimeZone.forID(site.getTimeZone());
    	} catch (IllegalArgumentException e){
    		log.warn("Time zone not correctly set for site. current value = " + site.getTimeZone());
    		dateTimeZone = DateTimeZone.UTC;
    	}
    	return dateTimeZone;
    }

    public boolean isAdminApp(){
    	if("ecadmin".equals(site)){
    		return true;
    	} else {
    		return false;
    	}
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

    public InputStream getResourceStream(String path, String property){
    	return repositoryTemplate.getNodePropertyStream(path, property);
    }

    //*************************
    // *** Album and Images ***
    private static final String ORIGINAL_ALBUM_NAME = "Originals";

    @SuppressWarnings("unchecked")
	public List<AlbumData> getAlbums(){
    	return (List<AlbumData>) repositoryTemplate.getAll(RepositoryType.ALBUM);
    }

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
    	return getAlbum(ORIGINAL_ALBUM_NAME);
	}

	public synchronized String createImage(IAlbumData album, String name, InputStream inputStream) {
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
	    Image original = (Image) repositoryTemplate.getOcmObject(Image.class, orginalAlbum.getId() + "/" + imageName);

	    String path = album.getId() + "/" + original.getName();
	    repositoryTemplate.copySave(original.getId(), path);
	    ImageHelper.resizeAlbumImage(template, path, album.getWidth(), album.getHeight());
	}

    public IImageData getImage(final String path) {
        if (StringUtils.isEmpty(path)){
            log.error("getImage - path is required.");
            return null;
        }
        return (IImageData) repositoryTemplate.getOcmObject(Image.class, path);
    }


    @SuppressWarnings("unchecked")
	public List<ImageData> getAlbumImages(AlbumData album){
        log.info("getAlbumImages - " + album);
    	List<ImageData> list = (List<ImageData>) repositoryTemplate.getAll(RepositoryType.IMAGE, album);
    	for (ImageData image : list){
    		image.setAlbumName(album.getName());
    	}
    	log.info("getAlbumImages - list items =" + list.size());
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
	    List<ImageData> list = getAlbumImages(album);
	    Collections.sort(list);
	    return list;
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
        return (BlogEntry) repositoryTemplate.getOcmObject(BlogEntry.class, path);
    }

    public void saveBlogEntry(BlogEntryData blogEntry){
    	saveDataObject(blogEntry);
    }

	@SuppressWarnings("unchecked")
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

    private PageMetaData getPageMeta(final PageData page) {
    	return (PageMetaData) repositoryTemplate.getOcmObject(RepositoryType.PAGEMETA, page, PageMeta.NAME, new RepositoryTemplateCallback(){

            @Override
            public AbstractData createNew(String parentPath, String name) {
                return new PageMeta(page);
            }

        });
    }

    public PageMetaLangData getPageMetaLang(final Locale locale, final PageData page) {
    	return getPageMetaLang(locale, page, true);
    }

    public PageMetaLangData getPageMetaLang(final Locale locale, final PageData page, boolean display) {
    	final PageMetaData pageMeta = getPageMeta(page);
    	SiteLanguage siteLang;
    	if (display) {
    		siteLang = getSiteLanguage(locale);
    	} else {
    		siteLang = SiteLanguage.getLanguage(locale.getLanguage());
    	}
    	return (PageMetaLangData) repositoryTemplate.getOcmObject(RepositoryType.PAGEMETALANG, pageMeta, siteLang.getDisplayName(), new RepositoryTemplateCallback(){

            @Override
            public AbstractData createNew(String parentPath, String name) {
                PageMetaLang lang =  new PageMetaLang(pageMeta, name);
                lang.setLang(locale.getLanguage());
                return lang;
            }

        });
    }

    public ContentData getContent(final PageData page, final String contentName){
    	return (Content) repositoryTemplate.getOcmObject(RepositoryType.CONTENT, page, contentName, new RepositoryTemplateCallback(){

			@Override
			public AbstractData createNew(String parentPath, String name) {
				return new Content(page, contentName);
			}

    	});
    }

    public Content getContent(final String id) {
		return (Content) repositoryTemplate.getOcmObject(Content.class, id);
	}

    public String getContentText(final ContentData parent, Locale locale) {
        return getContentEntry(parent, locale).getText();
    }

    public ContentEntryData getInlineContentEntry(final ContentData parent, final Locale locale){
    	SiteLanguage language = getSiteLanguage(locale);
    	return getContentEntry(parent, language.getDisplayName(), language.defaultInlineContent);
    }

    public ContentEntryData getContentEntry(final ContentData parent, final Locale locale){
    	SiteLanguage language = getSiteLanguage(locale);
    	return getContentEntry(parent, language.getDisplayName(), language.defaultContent);
    }

    public ContentEntryData getContentEntry(final ContentData parent, String name, final String defaultText){
    	return (ContentEntryData) repositoryTemplate.getOcmObject(RepositoryType.CONTENTENTRY, parent, name, new RepositoryTemplateCallback(){

			@Override
			public AbstractData createNew(String parentPath, String name) {
				ContentEntry contentEntry = new ContentEntry(parent, name);
				contentEntry.setText(defaultText);
				return contentEntry;
			}

    	});
    }

    private SiteLanguage getSiteLanguage(Locale locale){
    	String langString = getSiteData().getLocales();
    	boolean supported = StringUtils.contains(langString, locale.getLanguage());
    	final SiteLanguage language;
    	if (supported){
    		language = SiteLanguage.getLanguage(locale.getLanguage());
    	} else {
    		language = SiteLanguage.getLanguage(SiteLanguage.BASE_LOCALE.getLanguage());
    	}
    	return language;
    }

    //Resources
    public ResourceData getContentResource(final PageData page, final String name){
    	Resource data = (Resource) repositoryTemplate.getOcmObject(RepositoryType.RESOURCE, page, name, new RepositoryTemplateCallback(){

            @Override
			public AbstractData createNew(String parentPath, String name) {
            	log.debug("getContentResource - creating new Resource.");
				return new Resource(page, name);
			}

    	});
    	return data;
    }

    public ResourceData getContentResource(final String id){
    	return (ResourceData) repositoryTemplate.getOcmObject(Resource.class, id);
    }

    public void saveContentResource(final ResourceData data){
    	saveDataObject(data);
    	log.debug("saveContentResource - saved Data." + data);
    	if (data.getInputStream() != null){
    	    repositoryTemplate.saveNodePropertyStream(data.getId(), "inputStream", data.getInputStream());
    	    log.debug("saveContentResource - saved InputStream.");
    	}
    }

    //Video PLayer
    public VideoPlayerData getVideoPlayerData(final PageData page, final String name){
    	return (VideoPlayerData) repositoryTemplate.getOcmObject(RepositoryType.VIDEO, page, name, new RepositoryTemplateCallback(){

			@Override
			public AbstractData createNew(String parentPath, String name) {
				VideoPlayer data = new VideoPlayer(page, name);
				data.setVideoId("VIDEOID");
				data.setWidth(480);
				data.setHeight(385);
				return data;
			}

    	});
    }

    public void saveVideoPlayerData(final VideoPlayerData data){
    	saveDataObject(data);
    }

    //*********************************
    //******  Data    *****************
	public List<LogData> getTransactionLogEntries(){
		return transactionLogDao.fetch();
	}

    public void createEmailEntry(String name, String email, String comment){
    	EmailEntry data = new EmailEntry();
    	data.setSenderName(name);
    	data.setSenderEmailAddress(email);
    	data.setSenderComment(comment);
    	log.debug("creating email entry in data. " + data);
    	emailEntryDao.create(data);
    }

	public void deleteEmailEntry(EmailEntryData email) {
		emailEntryDao.delete(email);
	}

	public EmailEntryData getEmailEntry(String id) {
		if (StringUtils.isEmpty(id)) {
			log.error("getEmailEntry - id is required.");
			return null;
		}
		return emailEntryDao.find(id);
	}

	public List<EmailEntryData> getEmailEntries(){
		return emailEntryDao.fetch();
    }

	//**********************************
    // ******   Site              ******

	public SiteData getSiteData(){
        return getSite(site);
    }

    public SiteData getSite(String name){
    	return (SiteData) repositoryTemplate.getParentObject(RepositoryType.SITE, name, new RepositoryTemplateCallback(){

    		@Override
			public AbstractData createNew(String parentPath, String name) {
    			log.debug("createNew - creating Site :"+name);
    			return new Site(parentPath, name);
			}

    	});
    }

    @SuppressWarnings("unchecked")//another not-safe cast from Collection to List
	public List<SiteData> getSiteEntries(){
    	return (List<SiteData>) repositoryTemplate.getAll(RepositoryType.SITE);
    }

    //**********************************
    // ******   Member            ******
    public boolean isMemberExist(String name){
    	return memberService.isMemberExists(name);
    }

    public boolean saveMember(MemberData member){
    	return memberService.saveMember(member);
    }

    public IPasswordAuthenticator getMemberAuthenticator(String name){
    	return memberService.getPasswordAuthenticator(name);
    }

    public List<? extends MemberData> getMemberEntries(){
    	return memberService.getMembers();
    }

    public MemberData getMember(String memberId){
    	return memberService.getMember(memberId);
    }

    public MemberData getMemberById(String id){
    	return memberService.getMemberById(id);
    }

    public void deleteMember(MemberData data){
    	memberService.deleteMember(data);
    }
    
	public boolean saveMemberPassword(String memberId, String password) {
		return memberService.saveMemberPassword(memberId, password);
	}

    //**********************************
    // ******   Users             ******

    public ProfileData getProfile(String identifier){
    	return userSecurityService.getUserProfile(identifier);
    }

    public ProfileData getNewUserProfile(IUser user, String name, String identifier){
    	return userSecurityService.getNewUserProfile(user, name, identifier);
    }

    public UserData getUser(ProfileData profile){
    	return userSecurityService.getUser(profile);
    }

	public IPasswordAuthenticator getPasswordAuthenticator(String username) {
		return userSecurityService.getPasswordAuthenticator(username);
	}

    public UserData getNewUser(String username, String password){
    	return userSecurityService.getNewUser(username, password);
    }

    public UserData getUser(String username){
    	return userSecurityService.getUser(username);
    }

	public List<UserData> getUsers(){
		return userSecurityService.getUsers();
	}

	public boolean isUserSite(UserData userData){
		return userSecurityService.isUserSite(userData);
	}

	public IAuthenticator getUserAuthenticator() {
		return userSecurityService.getUserAuthenticator();
	}

	public void saveUserSite(UserSiteData data){
		userSecurityService.saveUserSite(data);
	}

	public UserSiteData getUserSite(UserData user){
		return userSecurityService.getUserSite(user);
	}

	public List<UserSiteData> getUserSiteEntries(UserData user){
		return userSecurityService.getUserSiteEntries(user);
	}

	public void saveUserSiteEntries(UserData user, List<SiteData> sites, boolean auth){
		userSecurityService.saveUserSiteEntries(user, sites, auth);
	}

    //************************************
    // *****  Utility methods

	public void saveDataObject(AbstractData data) {
		repositoryTemplate.saveDataObject(data);

	}

	public void setRepositoryTemplate(RepositoryTemplate repositoryTemplate) {
		this.repositoryTemplate = repositoryTemplate;
	}

	public RepositoryTemplate getRepositoryTemplate(){
		return repositoryTemplate;
	}

    //************************************
    // *****  Instanciation

	public void setEmailEntryDao(EmailEntryDao emailEntryDao) {
		this.emailEntryDao = emailEntryDao;
	}

	public void setTransactionLogDao(TransactionLogDao transactionLogDao) {
		this.transactionLogDao = transactionLogDao;
	}

    public void setTemplate(JcrTemplate template) {
        this.template = template;
    }

	public void setSite(String site) {
		this.site = site;
	}

	public void setUserSecurityService(UserSecurityService userSecurityService){
		this.userSecurityService = userSecurityService;
	}

	public void setMemberService(MemberService memberService) {
		this.memberService = memberService;
	}

}
