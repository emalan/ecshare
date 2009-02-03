package com.madalla.webapp;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

import com.madalla.bo.security.UserData;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.cms.IContentAdmin;
import com.madalla.webapp.security.IAuthenticator;

public abstract class CmsSession  extends WebSession implements IContentAdmin{

	private static final long serialVersionUID = 652426659740076486L;
	private boolean cmsAdminMode = false;
	private String username = null;
    
    public CmsSession(Request request) {
        super(request);
    }

    public boolean isCmsAdminMode() {
        return cmsAdminMode;
    }
    
    public boolean isLoggedIn() {
		return username != null;
	}

	public String getUsername(){
    	return username;
    }
    
    public final void logout(){
    	cmsAdminMode = false;
    	username = null;
    }
    
    public boolean login(String userName, String password) {
    	IRepositoryServiceProvider repositoryProvider =((IRepositoryServiceProvider)getApplication());
        IAuthenticator authenticator = repositoryProvider.getRepositoryService().getUserAuthenticator();
        if (authenticator.authenticate(userName, password)){
        	UserData user = repositoryProvider.getRepositoryService().getUser(userName);
            cmsAdminMode = (user.getAdmin()==null) ? false : user.getAdmin() ;
        	this.username = userName;
            return true;
        }
        return false;
    }
}
