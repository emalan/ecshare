package com.madalla.webapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.protocol.http.WebApplication;

import com.madalla.service.blog.IBlogService;
import com.madalla.service.blog.IBlogServiceProvider;
import com.madalla.service.cms.IContentAdminService;
import com.madalla.service.cms.IContentAdminServiceProvider;
import com.madalla.service.cms.IContentService;
import com.madalla.service.cms.IContentServiceProvider;

public abstract class CmsBlogApplication extends WebApplication implements IContentServiceProvider, IBlogServiceProvider, IContentAdminServiceProvider {

    private IContentService contentService;
    private IBlogService blogService;
    private IContentAdminService contentAdminService;

    
    protected void init() {
        
        getRequestCycleSettings().setGatherExtendedBrowserInfo(true);
        //getMarkupSettings().setStripWicketTags(true);
        
//        SimplePageAuthorizationStrategy authorizationStrategy = new SimplePageAuthorizationStrategy(
//                ContentEditPage.class, Home.class) {
//            protected boolean isAuthorized() {
//                return (((AppSession)Session.get()).isCmsAdminMode());
//            }
//        };
// 
//        getSecuritySettings().setAuthorizationStrategy(authorizationStrategy);
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
