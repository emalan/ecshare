package com.madalla.webapp;


import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.html.IPackageResourceGuard;
import org.apache.wicket.markup.html.PackageResourceGuard;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.settings.IExceptionSettings;
import org.emalan.cms.IDataService;
import org.emalan.cms.IDataServiceProvider;
import org.emalan.cms.IRepositoryAdminService;
import org.emalan.cms.IRepositoryAdminServiceProvider;
import org.emalan.cms.ISessionDataService;
import org.emalan.cms.RepositoryServiceFactory;
import org.emalan.cms.bo.SiteLanguage;
import org.emalan.cms.bo.image.AlbumData;
import org.emalan.cms.bo.image.ImageData;
import org.emalan.cms.bo.page.PageData;
import org.emalan.cms.bo.page.PageMetaLangData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import wicket.contrib.tinymce.TinyMceBehavior;

import com.madalla.member.service.ApplicationService;
import com.madalla.member.service.ApplicationServiceImpl;
import com.madalla.webapp.admin.content.ContentAdminPanel;
import com.madalla.webapp.admin.image.ImageAdminPanel;
import com.madalla.webapp.admin.member.MemberAdminPanel;
import com.madalla.webapp.admin.pages.AdminErrorPage;
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



/**
 * Abstract Wicket Application class that needs to extended to enable usage
 * of the Content Panels.
 *
 * @author Eugene Malan
 *
 */
public abstract class CmsApplication extends AuthenticatedCmsApplication implements IDataServiceProvider, IRepositoryAdminServiceProvider {

	public static final String SECURE_PASSWORD = "securePassword";
	public static final String PASSWORD = "password";
	public static final String MEMBER_REGISTRATION = "memberRegistration";
	public static final String MEMBER_PASSWORD = "memberPassword";
	public static final String LOGIN = "login";

	private static final Logger log = LoggerFactory.getLogger(CmsApplication.class);
	
	private final Marker fatal = MarkerFactory.getMarker("FATAL");
	
	private ApplicationService applicationService;
	
    private RuntimeConfigurationType configType;

    @Override
    protected void init() {
    	super.init();
        setupApplicationSpecificConfiguration();

    }
    
    @Override
	public Session newSession(Request request, Response response) {
    	ISessionDataService sessionService = RepositoryServiceFactory.getSessionDataService();
        return new CmsSession(request, sessionService);
    }

    private void setupApplicationSpecificConfiguration(){
    	
    	final IPackageResourceGuard packageResourceGuard = new PackageResourceGuard();
    	getResourceSettings().setPackageResourceGuard(packageResourceGuard);
    	packageResourceGuard.accept(TinyMceBehavior.class, "/tiny_mce/");
    	
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
					//TODO
					//mount(new I18NBookmarkablePageMapper(lang.locale, lang.getLanguageCode(), getHomePage()));
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
						//TODO
						//mount(new I18NBookmarkablePageMapper(lang.locale, mountName, page));
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

    protected abstract boolean isSiteMultilingual();

    public boolean hasMemberService(){
    	return false;
    }

    public IRepositoryAdminService getRepositoryAdminService() {
		return applicationService.getRepositoryAdminService();
	}

	public IDataService getRepositoryService() {
		return applicationService.getRepositoryService();
	}
	
	public ApplicationService getApplicationService() {
		return applicationService;
	}
	
	//////////////////////////////////////////////////////
	//       Initializing methods
	//////////////////////////////////////////////////////

	public void setConfigType(String configType) {
		if ("DEVELOPMENT".equalsIgnoreCase(configType)) {
			this.configType = RuntimeConfigurationType.DEVELOPMENT;
		} else {
			this.configType = RuntimeConfigurationType.DEVELOPMENT;
		}
	}

	public void setApplicationService(ApplicationServiceImpl applicationService) {
		this.applicationService = applicationService;
	}

}
