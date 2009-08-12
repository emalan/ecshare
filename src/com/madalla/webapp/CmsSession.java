package com.madalla.webapp;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

import com.madalla.bo.security.UserData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.cms.IContentAdmin;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.webapp.upload.IFileUploadInfo;
import com.madalla.webapp.upload.IFileUploadStatus;

public class CmsSession  extends WebSession implements IContentAdmin, IRepositoryServiceProvider, IFileUploadInfo{

	private static final long serialVersionUID = 652426659740076486L;
	private boolean cmsAdminMode = false;
	private String username = null;
	private AuthRepositoryService repositoryService;
	private volatile Map<String, IFileUploadStatus> fileUploadInfo;
    
    public CmsSession(Request request) {
        super(request);
        IRepositoryServiceProvider repositoryProvider =((IRepositoryServiceProvider)getApplication());
        repositoryService = new AuthRepositoryService();
        fileUploadInfo = new HashMap<String, IFileUploadStatus>();
    }

    public boolean isCmsAdminMode() {
        return cmsAdminMode;
    }
    
    public boolean isSuperAdmin() {
    	return username.equalsIgnoreCase("admin");
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
    	repositoryService.setUser(null);
    	fileUploadInfo.clear();
    }
    
    public boolean login(String userName, String password) {
    	IRepositoryServiceProvider repositoryProvider =((IRepositoryServiceProvider)getApplication());
        IAuthenticator authenticator = repositoryProvider.getRepositoryService().getUserAuthenticator();
        if (authenticator.authenticate(userName, password)){
        	UserData user = repositoryProvider.getRepositoryService().getUser(userName);
        	repositoryService.setUser(user);
            cmsAdminMode = (user.getAdmin()==null) ? false : user.getAdmin() ;
        	this.username = userName;
            return true;
        }
        return false;
    }
    
	public IRepositoryService getRepositoryService() {
		return repositoryService;
	}

	public IFileUploadStatus getFileUploadStatus(String id) {
		return fileUploadInfo.get(id);
	}

	public void setFileUploadStatus(String id, IFileUploadStatus status) {
		fileUploadInfo.put(id, status);
		
	}

	public void setFileUploadComplete(String id) {
		fileUploadInfo.remove(id);
	}


}
