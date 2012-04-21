package com.madalla.webapp;


import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.settings.IExceptionSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import com.madalla.BuildInformation;
import com.madalla.bo.SiteLanguage;
import com.madalla.bo.image.AlbumData;
import com.madalla.bo.image.ImageData;
import com.madalla.bo.page.PageData;
import com.madalla.bo.page.PageMetaLangData;
import com.madalla.email.IEmailSender;
import com.madalla.email.IEmailServiceProvider;
import com.madalla.service.IDataService;
import com.madalla.service.IDataServiceProvider;
import com.madalla.service.IRepositoryAdminService;
import com.madalla.service.IRepositoryAdminServiceProvider;
import com.madalla.webapp.admin.content.ContentAdminPanel;
import com.madalla.webapp.admin.image.ImageAdminPanel;
import com.madalla.webapp.admin.member.MemberAdminPanel;
import com.madalla.webapp.admin.pages.AdminErrorPage;
import com.madalla.webapp.admin.pages.AlbumAdminPage;
import com.madalla.webapp.admin.pages.MainAdminPage;
import com.madalla.webapp.admin.pages.SecurePasswordPage;
import com.madalla.webapp.admin.pages.UserLoginPage;
import com.madalla.webapp.admin.pages.UserPasswordPage;
import com.madalla.webapp.admin.site.PageAdminPanel;
import com.madalla.webapp.admin.site.SiteAdminPanel;
import com.madalla.webapp.admin.site.SiteDataPanel;
import com.madalla.webapp.admin.site.SiteEmailPanel;
import com.madalla.webapp.authorization.AuthenticatedCmsApplication;
import com.madalla.webapp.cms.editor.ContentEntryPanel;
import com.madalla.webapp.cms.editor.TranslatePanel;
import com.madalla.webapp.user.UserAdminPanel;
import com.madalla.webapp.user.UserProfilePanel;
import com.madalla.wicket.mount.I18NBookmarkablePageMapper;

/**
 * Abstract Wicket Application class that needs to extended to enable usage
 * of the Content Panels.
 *
 * @author Eugene Malan
 *
 */
public abstract class CmsApplication extends AuthenticatedCmsApplication implements IDataServiceProvider, IRepositoryAdminServiceProvider, IEmailServiceProvider {

	public static final String SECURE_PASSWORD = "securePassword";
	public static final String PASSWORD = "password";
	public static final String MEMBER_REGISTRATION = "memberRegistration";
	public static final String MEMBER_PASSWORD = "memberPassword";
	public static final String LOGIN = "login";

	protected final Logger log = LoggerFactory.getLogger(CmsApplication.class);
	private final Marker fatal = MarkerFactory.getMarker("FATAL");
	private IRepositoryAdminService repositoryAdminService;
    private IEmailSender emailSender;
    private IDataService dataService;
    private BuildInformation buildInformation;
    private RuntimeConfigurationType configType;

    @Override
    protected void init() {
    	super.init();
    	//initialization checks
    	
    	if (buildInformation == null) {
    		log.error(fatal, "Build Information not configured Correctly.");
    		throw new WicketRuntimeException("Build Information not configured Correctly.");
    	}
    	log.info("Build Information. ecshare version:" + buildInformation.getVersion());
    	if (repositoryAdminService == null){
    		log.error(fatal, "Content Admin Service is not configured Correctly.");
    		throw new WicketRuntimeException("Repository Admin Service is not configured Correctly.");
    	}
    	if (dataService == null){
    		log.error(fatal, "Repository Data Service is not configured Correctly.");
    		throw new WicketRuntimeException("Repository Data Service is not configured Correctly.");
    	}
    	if (emailSender == null){
    		log.error(fatal, "Email Sender is not configured Correctly.");
    		throw new WicketRuntimeException("Email Service is not configured Correctly.");
    	}
        setupApplicationSpecificConfiguration();

    }
    
    @Override
	public Session newSession(Request request, Response response) {
        return new CmsSession(request);
    }

    private void setupApplicationSpecificConfiguration(){
    	//getRequestCycleSettings().setGatherExtendedBrowserInfo(true);
    	setupPageMounts();
    	setupErrorHandling();
    	setupSecurity();
    }

    protected void setupPageMounts(){

    	final List<SiteLanguage> langs = SiteLanguage.getAllLanguages();

		if (isSiteMultilingual()) {
			// mount a home page for each language
			for (SiteLanguage lang : langs) {
				try {
					mount(new I18NBookmarkablePageMapper(lang.locale, lang.getLanguageCode(), getHomePage()));
				} catch (WicketRuntimeException e) {
					log.error("Error while mounting home landing page for language:" + lang, e);
				}
			}

			// mount application pages
			for (Class<? extends Page> page : getAppPages()) {
				final PageData pageData = getRepositoryService().getPage(page.getSimpleName());

				// mount rest of application pages
				for (SiteLanguage lang : langs) {
					PageMetaLangData pageInfo = getRepositoryService().getPageMetaLang(lang.locale, pageData, false);
					final String mountName = pageInfo.getMountName(pageData.getName());
					try {
						mount(new I18NBookmarkablePageMapper(lang.locale, mountName, page));
					} catch (WicketRuntimeException e) {
						log.error("Error while mounting Application Page with name:" + mountName, e);
					}
				}

			}

		} else {
			//Multi-lingual not supported
			for (Class<? extends Page> page : getAppPages()) {
				final PageData pageData = getRepositoryService().getPage(page.getSimpleName());
				PageMetaLangData pageInfo = getRepositoryService().getPageMetaLang(SiteLanguage.BASE_LOCALE, pageData,
						false);
				final String mountName = StringUtils.isEmpty(pageInfo.getDisplayName())? pageData.getName():
					StringUtils.deleteWhitespace(pageInfo.getDisplayName());
				try {
					mountPage(mountName, page);
				} catch (WicketRuntimeException e) {
					log.error("Error while mounting Application Page with name:" + mountName, e);
				}
			}
		}

    	mountPage(PASSWORD, UserPasswordPage.class);
    	mountPage(SECURE_PASSWORD, SecurePasswordPage.class);

    	mountPage("control", MainAdminPage.class);
    	
    	//TODO monunt album pages
    	//mount(new IndexedParamUrlCodingStrategy("admin/album", AlbumAdminPage.class));

    	//TODO mount login page
    	//mount(new MixedParamHybridUrlCodingStrategy(LOGIN, UserLoginPage.class, UserLoginPage.PARAMETERS));

    	if (hasMemberService()){
    		if (getMemberPasswordPage() == null) {
        		log.error(fatal, "Member Service not configured Correctly. Member Service Password Page is null.");
        		throw new WicketRuntimeException("Member Service not configured Correctly. Member Service Password Page is null.");
        	}
    		if (getMemberRegistrationPage() == null) {
        		log.error(fatal, "Member Service not configured Correctly. Member Service Registration Page is null.");
        		throw new WicketRuntimeException("Member Service not configured Correctly. Member Service Registration Page is null.");
        	}
    		mountPage(MEMBER_PASSWORD, getMemberPasswordPage());
    		mountPage(MEMBER_REGISTRATION, getMemberRegistrationPage());
    	}

    	//mount images
    	for(AlbumData album : getRepositoryService().getAlbums()){
    		for (ImageData image : getRepositoryService().getAlbumImages(album)){
    			mountImage(image);
    		}
    	}

    }
    
    //TODO switch https off in development mode or if sertificate not found

    /* (non-Javadoc)
     * @see org.apache.wicket.protocol.http.WebApplication#newRequestCycleProcessor()
     * Disable https when in development mode or when the application has no security certificate
     */
//    @Override
//    protected IRequestCycleProcessor newRequestCycleProcessor()
//    {
//    	HttpsConfig config = new HttpsConfig(80,443);
//        return new HttpsRequestCycleProcessor(config){
//
//			@Override
//			protected IRequestTarget checkSecureIncoming(IRequestTarget target) {
//				if (getConfigurationType().equals(Application.DEVELOPMENT) || !getRepositoryService().getSiteData().getSecurityCertificate()){
//					return target;
//				} else {
//					return super.checkSecureIncoming(target);
//				}
//			}
//
//			@Override
//			protected IRequestTarget checkSecureOutgoing(IRequestTarget target) {
//				if (getConfigurationType().equals(Application.DEVELOPMENT) || !getRepositoryService().getSiteData().getSecurityCertificate()){
//					return target;
//				} else {
//					return super.checkSecureOutgoing(target);
//				}
//			}
//
//
//
//        };
//    }

    @Override
	public Class<? extends WebPage> getMemberRegistrationPage(){
    	return null;
    }

    public Class<? extends WebPage> getMemberPasswordPage(){
    	return null;
    }

	protected void mountImage(ImageData image) {
    	getSharedResources().add(image.getResourceReference(), image.getImageFull());
		
    	//TODO mount images
    	//mountSharedResource("/" + image.getMountUrl(), Application.class.getName() + "/" +image.getResourceReference());
    }
    private void setupErrorHandling(){

    	if (RuntimeConfigurationType.DEPLOYMENT.equals(getConfigurationType())) {
        	//TODO create page for access denied exceptions
    		//TODO figure out why we get unexpected exception instead of access denied for generalAdminPage
        	//getApplicationSettings().setPageExpiredErrorPage(MyExpiredPage.class);
        	//getApplicationSettings().setAccessDeniedPage(MyAccessDeniedPage.class);
        	getApplicationSettings().setInternalErrorPage(getApplicationErrorPage());
        	getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
		}
    }

    protected void setupSecurity(){
    	MetaDataRoleAuthorizationStrategy.authorize(UserProfilePanel.class, "USER");
    	MetaDataRoleAuthorizationStrategy.authorize(SiteAdminPanel.class, "USER");
    	MetaDataRoleAuthorizationStrategy.authorize(SiteDataPanel.class, "USER");
    	MetaDataRoleAuthorizationStrategy.authorize(MemberAdminPanel.class, "USER");
    	MetaDataRoleAuthorizationStrategy.authorize(ImageAdminPanel.class, "ADMIN");
    	MetaDataRoleAuthorizationStrategy.authorize(ContentAdminPanel.class, "ADMIN");
    	MetaDataRoleAuthorizationStrategy.authorize(PageAdminPanel.class, "USER");
    	MetaDataRoleAuthorizationStrategy.authorize(SiteEmailPanel.class, "SUPERADMIN");
    	MetaDataRoleAuthorizationStrategy.authorize(UserAdminPanel.class, "SUPERADMIN");
    	MetaDataRoleAuthorizationStrategy.authorize(ContentEntryPanel.class, "USER");
    	MetaDataRoleAuthorizationStrategy.authorize(TranslatePanel.class, "USER");
    }

	@Override
	protected Class<? extends WebPage> getSignInPageClass() {
		return UserLoginPage.class;
	}

	protected Class<? extends WebPage> getApplicationErrorPage(){
		return AdminErrorPage.class;
	}

	@Override
	protected Class<? extends AuthenticatedWebSession> getWebSessionClass() {
		return CmsSession.class;
	}

    @Override
	public boolean isMemberAuthorized() {
		return CmsSession.get().getMemberSession().isSignedIn();
	}

	/* (non-Javadoc)
     * @see org.apache.wicket.protocol.http.WebApplication#getConfigurationType()
     */
    @Override
	public RuntimeConfigurationType getConfigurationType() {
    	if (configType == null){
    		return super.getConfigurationType();
    	} else {
    		return configType;
    	}
	}

    /**
     * @return all application pages, make sure to include home page
     */
    final public Collection<Class <? extends Page>> getAppPages(){
    	Collection<Class<? extends Page>> pages = new LinkedHashSet<Class<? extends Page>>();
    	addAppPages(pages);
    	return pages;
    }

    protected abstract void addAppPages(Collection<Class<? extends Page>> pages);

    /**
     * Supplies a Collection with Pages for the menu
     *
     * @return List of Pages used in the menu OR Collections.emptyList() if no menu
     */
    final public Collection<Class<? extends Page>> getPageMenuList(){
    	Collection<Class<? extends Page>> pages = new LinkedHashSet<Class<? extends Page>>();
    	addPagesForMenu(pages);
    	return pages;
    }

    /**
     * Implementing classes should add the menu pages to the supplied Collection so that they
     * can be displayed on the menu
     */
    protected abstract void addPagesForMenu(Collection<Class<? extends Page>> pages);
    public boolean hasRpxService(){
    	return false;
    }

    protected abstract boolean isSiteMultilingual();

    public boolean hasMemberService(){
    	return false;
    }

    public IEmailSender getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(IEmailSender emailSender) {
        this.emailSender = emailSender;
    }

    public IRepositoryAdminService getRepositoryAdminService() {
		return repositoryAdminService;
	}

	public void setRepositoryAdminService(
			IRepositoryAdminService repositoryAdminService) {
		this.repositoryAdminService = repositoryAdminService;
	}

	public IDataService getRepositoryService() {
		return dataService;
	}

	public void setRepositoryService(IDataService dataService){
		this.dataService = dataService;
	}

	public void setBuildInformation(BuildInformation buildInformation) {
		this.buildInformation = buildInformation;
	}

	public BuildInformation getBuildInformation(){
		return buildInformation;
	}

	public void setConfigType(String configType) {
		if ("DEVELOPMENT".equalsIgnoreCase(configType)) {
			this.configType = RuntimeConfigurationType.DEVELOPMENT;
		} else {
			this.configType = RuntimeConfigurationType.DEVELOPMENT;
		}
	}

}
