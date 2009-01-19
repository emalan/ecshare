package com.madalla.webapp;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

import com.madalla.cms.service.IRepositoryAdminServiceProvider;
import com.madalla.webapp.cms.IContentAdmin;
import com.madalla.webapp.security.IAuthenticator;

public abstract class CmsSession  extends WebSession implements IContentAdmin{

	private static final long serialVersionUID = 652426659740076486L;
	private boolean cmsAdminMode = false;
    
    public CmsSession(Request request) {
        super(request);
    }

    public boolean isCmsAdminMode() {
        return cmsAdminMode;
    }
    
    public void setCmsAdminMode(boolean cmsAdminMode) {
        this.cmsAdminMode = cmsAdminMode;
    }
    
    public boolean login(String userName, String password) {
    	IRepositoryAdminServiceProvider adminService =((IRepositoryAdminServiceProvider)getApplication());
        IAuthenticator authenticator = adminService.getRepositoryAdminService().getUserAuthenticator();
        if (authenticator.authenticate(userName, password)){
            setCmsAdminMode(true);
            return true;
        }
        return false;
    }
}
