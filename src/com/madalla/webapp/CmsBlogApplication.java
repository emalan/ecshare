package com.madalla.webapp;


import org.apache.wicket.Session;
import org.apache.wicket.authorization.strategies.page.SimplePageAuthorizationStrategy;
import org.apache.wicket.protocol.http.WebApplication;

import com.madalla.service.blog.IBlogService;
import com.madalla.service.blog.IBlogServiceProvider;
import com.madalla.service.cms.IContentAdminService;
import com.madalla.service.cms.IContentAdminServiceProvider;
import com.madalla.service.cms.IContentService;
import com.madalla.service.cms.IContentServiceProvider;

/**
 * Abstract Wicket Application class that needs to extended to enable usage 
 * of the Wicket {@link Panels} provided by the Panels class.
 *  
 * @author Eugene Malan
 *
 */
public abstract class CmsBlogApplication extends WebApplication implements IContentServiceProvider, IBlogServiceProvider, IContentAdminServiceProvider {

    private IContentService contentService;
    private IBlogService blogService;
    private IContentAdminService contentAdminService;

    
    protected void init() {
        getRequestCycleSettings().setGatherExtendedBrowserInfo(true);
        //getMarkupSettings().setStripWicketTags(true);
        setupSecurity();
    }
    
    protected void setupSecurity(){
        SimplePageAuthorizationStrategy authorizationStrategy = new SimplePageAuthorizationStrategy(
                ISecureWebPage.class, getHomePage()) {
            protected boolean isAuthorized() {
                return ((CmsSession)Session.get()).isCmsAdminMode();
            }
        };
 
        getSecuritySettings().setAuthorizationStrategy(authorizationStrategy);
    }
    
    public IContentService getContentService(){
        return contentService;
    }
    
    public void setContentService(IContentService contentService){
        this.contentService = contentService;
    }

    public void setBlogService(IBlogService blogService) {
        this.blogService = blogService;
    }

    public IBlogService getBlogService() {
        return blogService;
    }

    public IContentAdminService getContentAdminService() {
        return contentAdminService;
    }

    public void setContentAdminService(IContentAdminService contentAdminService) {
        this.contentAdminService = contentAdminService;
    }

}
