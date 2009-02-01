package com.madalla.webapp;


import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Session;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.protocol.http.WebApplication;

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
public abstract class CmsApplication extends WebApplication implements IRepositoryServiceProvider, IRepositoryAdminServiceProvider {

    private IRepositoryService repositoryService;
    private IRepositoryAdminService repositoryAdminService;

	private final static Log log = LogFactory.getLog(CmsApplication.class);
    
    protected void init() {
    	//initialization checks
    	if (repositoryService == null){
    		log.fatal("Content Service is not configured Correctly.");
    		throw new WicketRuntimeException("Service is not configured Correctly.");
    	}
    	if (repositoryAdminService == null){
    		log.fatal("Content Admin Service is not configured Correctly.");
    		throw new WicketRuntimeException("Service is not configured Correctly.");
    	}
        setupApplicationSpecificConfiguration();
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
    	
    	//create Authorization strategy
    	AppAuthorizationStrategy authorizationStrategy = new AppAuthorizationStrategy(
                getHomePage(), pageAuthorizations);
 
        getSecuritySettings().setAuthorizationStrategy(authorizationStrategy);
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
