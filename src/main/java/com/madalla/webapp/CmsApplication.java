package com.madalla.webapp;

import java.awt.image.BufferedImage;
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
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.settings.IExceptionSettings;
import org.emalan.cms.IDataService;
import org.emalan.cms.IRepositoryAdminService;
import org.emalan.cms.ISessionDataService;
import org.emalan.cms.bo.SiteLanguage;
import org.emalan.cms.bo.image.AlbumData;
import org.emalan.cms.bo.image.ImageData;
import org.emalan.cms.bo.page.PageData;
import org.emalan.cms.bo.page.PageMetaLangData;
import org.emalan.cms.ocm.SessionDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import wicket.contrib.tinymce.TinyMceBehavior;

import com.madalla.service.ApplicationService;
import com.madalla.service.IApplicationServiceProvider;
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
import com.madalla.wicket.mount.I18NBookmarkablePageMapper;
import com.madalla.wicket.request.AppHttpsMapper;

/**
 * Abstract Wicket Application class that needs to extended to enable usage of
 * the Content Panels.
 * 
 * @author Eugene Malan
 * 
 */
public abstract class CmsApplication extends AuthenticatedCmsApplication implements IApplicationServiceProvider {

    public static final String SECURE_PASSWORD = "securePassword";
    public static final String PASSWORD = "password";
    public static final String MEMBER_REGISTRATION = "memberRegistration";
    public static final String MEMBER_PASSWORD = "memberPassword";
    public static final String LOGIN = "login";

    private int http = 80;
    private int https = 443;

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
        ISessionDataService sessionService = new SessionDataService();
        return new CmsSession(request, sessionService);
    }

    public void mountApplicationPage(final PageData pageData, final PageMetaLangData newPageInfo,
            final String oldName) {

        final String pageName = pageData.getName();

        if (isSiteMultilingual()) {
            String lang = newPageInfo.getLang();
            
            String mountName = StringUtils.isEmpty(newPageInfo.getDisplayName()) ? lang + "/" + pageName : lang + "/"
                    + StringUtils.deleteWhitespace(newPageInfo.getDisplayName());
            
            if (StringUtils.isNotEmpty(oldName)) {
            	String existingMount = StringUtils.isEmpty(oldName) ? null : lang + "/" + oldName;
            	unmount(existingMount);
            }
            
            SiteLanguage siteLanguage = SiteLanguage.getLanguage(lang);
            mount(new I18NBookmarkablePageMapper(mountName, siteLanguage.locale, getPageClass(pageName)));
        } else {
            String mountName = StringUtils.isEmpty(newPageInfo.getDisplayName()) ? pageName : StringUtils
                    .deleteWhitespace(newPageInfo.getDisplayName());
            
            log.debug("mountApplicationPage - page:" + pageName + " existing:" + oldName + " new:" + mountName);
            if (StringUtils.isNotEmpty(oldName)) {
                unmount(oldName);
            }
            try {
                mountPage(mountName, getPageClass(pageName));
            } catch (WicketRuntimeException e) {
                log.error("Error while mounting Application Page.", e);
            }

        }


    }

    private void setupApplicationSpecificConfiguration() {

        final IPackageResourceGuard packageResourceGuard = new PackageResourceGuard();
        getResourceSettings().setPackageResourceGuard(packageResourceGuard);
        packageResourceGuard.accept(TinyMceBehavior.class, "/tiny_mce/");

        setupPageMounts();
        setupErrorHandling();
        setupSecurity();

        HttpsConfig config = new HttpsConfig(http, https);
        setRootRequestMapper(new AppHttpsMapper(getRootRequestMapper(), getConfigurationType(), config) {

            @Override
            public boolean isSiteSecure() {
                return applicationService.isSiteSecure();
            }

        });
        
    }

    protected void setupPageMounts() {

        final List<SiteLanguage> langs = SiteLanguage.getAllLanguages();

        if (isSiteMultilingual()) {

            // mount a mapper for each page
            for (Class<? extends Page> page : getAppPages()) {
                final PageData pageData = getRepositoryService().getPage(page.getSimpleName());

                // get data for each lang
                for (SiteLanguage lang : langs) {
                    PageMetaLangData pageInfo = getRepositoryService().getPageMetaLang(lang.locale, pageData, false);
                    log.trace("mounting page - " + pageInfo);
                    mountApplicationPage(pageData, pageInfo, null);
                }
            }

        } else {
            // Multi-lingual not supported, mount app pages without locale path
            for (Class<? extends Page> page : getAppPages()) {
                final PageData pageData = getRepositoryService().getPage(page.getSimpleName());
                PageMetaLangData pageInfo = getRepositoryService().getPageMetaLang(SiteLanguage.BASE_LOCALE, pageData,
                        false);
                mountApplicationPage(pageData, pageInfo, null);
            }
        }

        mountPage(PASSWORD, UserPasswordPage.class);
        mountPage(SECURE_PASSWORD, SecurePasswordPage.class);

        mountPage("control", MainAdminPage.class);

        // TODO mount album pages
        // mount(new IndexedParamUrlCodingStrategy("admin/album",
        // AlbumAdminPage.class));

        // TODO mount login page
        // mount(new MixedParamHybridUrlCodingStrategy(LOGIN,
        // UserLoginPage.class, UserLoginPage.PARAMETERS));

        if (hasMemberService()) {
            if (getMemberPasswordPage() == null) {
                log.error(fatal, "Member Service not configured Correctly. Member Service Password Page is null.");
                throw new WicketRuntimeException(
                        "Member Service not configured Correctly. Member Service Password Page is null.");
            }
            if (getMemberRegistrationPage() == null) {
                log.error(fatal, "Member Service not configured Correctly. Member Service Registration Page is null.");
                throw new WicketRuntimeException(
                        "Member Service not configured Correctly. Member Service Registration Page is null.");
            }
            mountPage(MEMBER_PASSWORD, getMemberPasswordPage());
            mountPage(MEMBER_REGISTRATION, getMemberRegistrationPage());
        }

        // mount images
        for (AlbumData album : getRepositoryService().getAlbums()) {
            for (ImageData image : getRepositoryService().getAlbumImages(album)) {
                mountImage(image);
            }
        }

    }

    private Class<? extends Page> getPageClass(String name) {
        Collection<Class<? extends Page>> pages = getAppPages();
        for (Class<? extends Page> page : pages) {
            if (page.getSimpleName().equals(name)) {
                return page;
            }
        }
        return null;
    }

    protected void mountImage(ImageData image) {
        mountImage(image.getImageFull(), image.getResourceReference(), image.getMountUrl());
        mountImage(image.getImageThumb(), image.getThumbResourceReference(), image.getThumbMountUrl());
    }

    private void mountImage(final BufferedImage image, final String resourceReference, final String mountUrl) {
        final BufferedDynamicImageResource resource = new BufferedDynamicImageResource();
        resource.setImage(image);
        log.debug("mountImage - " + mountUrl);
        mountResource("/" + mountUrl, new ResourceReference(resourceReference) {

            private static final long serialVersionUID = 1L;

            @Override
            public IResource getResource() {
                return resource;
            }

        });
    }

    private void setupErrorHandling() {

        if (RuntimeConfigurationType.DEPLOYMENT.equals(getConfigurationType())) {
            // TODO create page for access denied exceptions
            // TODO figure out why we get unexpected exception instead of access
            // denied for generalAdminPage
            // getApplicationSettings().setPageExpiredErrorPage(MyExpiredPage.class);
            // getApplicationSettings().setAccessDeniedPage(MyAccessDeniedPage.class);
            getApplicationSettings().setInternalErrorPage(getApplicationErrorPage());
            getExceptionSettings().setUnexpectedExceptionDisplay(IExceptionSettings.SHOW_INTERNAL_ERROR_PAGE);
        }
    }

    protected void setupSecurity() {
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
    public Class<? extends WebPage> getMemberRegistrationPage() {
        return null;
    }

    public Class<? extends WebPage> getMemberPasswordPage() {
        return null;
    }

    @Override
    protected Class<? extends WebPage> getSignInPageClass() {
        return UserLoginPage.class;
    }

    protected Class<? extends WebPage> getApplicationErrorPage() {
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

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.wicket.protocol.http.WebApplication#getConfigurationType()
     */
    @Override
    public RuntimeConfigurationType getConfigurationType() {
        if (configType == null) {
            return super.getConfigurationType();
        } else {
            return configType;
        }
    }

    /**
     * @return all application pages, make sure to include home page
     */
    final public Collection<Class<? extends Page>> getAppPages() {
        Collection<Class<? extends Page>> pages = new LinkedHashSet<Class<? extends Page>>();
        addAppPages(pages);
        return pages;
    }

    protected abstract void addAppPages(Collection<Class<? extends Page>> pages);

    /**
     * Supplies a Collection with Pages for the menu
     * 
     * @return List of Pages used in the menu OR Collections.emptyList() if no
     *         menu
     */
    final public Collection<Class<? extends Page>> getPageMenuList() {
        Collection<Class<? extends Page>> pages = new LinkedHashSet<Class<? extends Page>>();
        addPagesForMenu(pages);
        return pages;
    }

    /**
     * Implementing classes should add the menu pages to the supplied Collection
     * so that they can be displayed on the menu
     */
    protected abstract void addPagesForMenu(Collection<Class<? extends Page>> pages);

    public abstract boolean isSiteMultilingual();

    public boolean hasMemberService() {
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

    // ////////////////////////////////////////////////////
    // Initializing methods
    // ////////////////////////////////////////////////////

    public void setConfigType(String configType) {
        if ("DEVELOPMENT".equalsIgnoreCase(configType)) {
            this.configType = RuntimeConfigurationType.DEVELOPMENT;
        } else {
            this.configType = RuntimeConfigurationType.DEPLOYMENT;
        }
    }

    public void setApplicationService(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    public void setHttp(int http) {
        this.http = http;
    }

}
