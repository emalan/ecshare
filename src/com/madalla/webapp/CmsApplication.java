package com.madalla.webapp;


import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.https.HttpsConfig;
import org.apache.wicket.protocol.https.HttpsRequestCycleProcessor;
import org.apache.wicket.request.IRequestCycleProcessor;

import com.madalla.email.IEmailSender;
import com.madalla.email.IEmailServiceProvider;
import com.madalla.service.IRepositoryAdminService;
import com.madalla.service.IRepositoryAdminServiceProvider;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.authorization.AppAuthorizationStrategy;
import com.madalla.webapp.authorization.PageAuthorization;

/**
 * Abstract Wicket Application class that needs to extended to enable usage 
 * of the Wicket {@link Panels} provided by the Panels class.
 *  
 * @author Eugene Malan
 *
 */
public abstract class CmsApplication extends WebApplication implements IRepositoryServiceProvider, IRepositoryAdminServiceProvider, IEmailServiceProvider {

	private final static Log log = LogFactory.getLog(CmsApplication.class);

	private IRepositoryService repositoryService;
    private IRepositoryAdminService repositoryAdminService;
    private IEmailSender emailSender;

    
    protected void init() {
    	//initialization checks
    	if (repositoryService == null){
    		log.fatal("Content Service is not configured Correctly.");
    		throw new WicketRuntimeException("Repository Service is not configured Correctly.");
    	}
    	if (repositoryAdminService == null){
    		log.fatal("Content Admin Service is not configured Correctly.");
    		throw new WicketRuntimeException("Repository Admin Service is not configured Correctly.");
    	}
    	if (emailSender == null){
    		log.fatal("Email Sender is not configured Correctly.");
    		throw new WicketRuntimeException("Email Service is not configured Correctly.");
    	}
        setupApplicationSpecificConfiguration();
    }
    
    public Session newSession(Request request, Response response) {
        return new CmsSession(request);
    }
    
    private void setupApplicationSpecificConfiguration(){
    	//getRequestCycleSettings().setGatherExtendedBrowserInfo(true);
    	setupSecurity();
    }
    
    protected void setupSecurity(){
    	
    	//List to hold page authorizations
    	Collection <PageAuthorization> pageAuthorizations = new ArrayList<PageAuthorization>();
    	
    	//Admin Page authorization
    	PageAuthorization adminAuthorization = new PageAuthorization(ISecureAdminPage.class){
			@Override
			protected boolean isAuthorized() {
                return ((CmsSession)Session.get()).isCmsAdminMode();
            }
    	};
    	pageAuthorizations.add(adminAuthorization);

    	//Logged in page authorization
    	PageAuthorization loggedInAuthorization = new PageAuthorization(ISecureWebPage.class){
			@Override
			protected boolean isAuthorized() {
                return ((CmsSession)Session.get()).isLoggedIn();
            }
    	};
    	pageAuthorizations.add(loggedInAuthorization);
    	
    	//Super Admin page authorization
    	PageAuthorization superAdminAthorization = new PageAuthorization(ISecureSuperPage.class){
    		@Override
    		protected boolean isAuthorized() {
    			return ((CmsSession)Session.get()).isSuperAdmin();
    		}
    	};
    	pageAuthorizations.add(superAdminAthorization);
    	
    	//create Authorization strategy
    	AppAuthorizationStrategy authorizationStrategy = new AppAuthorizationStrategy(
                getHomePage(), pageAuthorizations);
 
        getSecuritySettings().setAuthorizationStrategy(authorizationStrategy);
    }
    
    @Override
    protected IRequestCycleProcessor newRequestCycleProcessor()
    {
    	HttpsConfig config = new HttpsConfig(8081,8443);
            return new HttpsRequestCycleProcessor(config);
    }

    public IEmailSender getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(IEmailSender emailSender) {
        this.emailSender = emailSender;
    }
    
    public IRepositoryService getRepositoryService(){
        return repositoryService;
    }
    
    public void setRepositoryService(IRepositoryService repositoryService){
        this.repositoryService = repositoryService;
    }

    public IRepositoryAdminService getRepositoryAdminService() {
		return repositoryAdminService;
	}

	public void setRepositoryAdminService(
			IRepositoryAdminService repositoryAdminService) {
		this.repositoryAdminService = repositoryAdminService;
	}

}
