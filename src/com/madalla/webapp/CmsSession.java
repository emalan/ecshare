package com.madalla.webapp;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

import com.madalla.bo.security.UserData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.cms.IContentAdmin;
import com.madalla.webapp.cms.IFileUploadStatus;
import com.madalla.webapp.security.IAuthenticator;

public class CmsSession  extends WebSession implements IContentAdmin, IRepositoryServiceProvider, IFileUploadStatus{

	private static final long serialVersionUID = 652426659740076486L;
	private boolean cmsAdminMode = false;
	private String username = null;
	private AuthRepositoryService repositoryService;
	private volatile boolean uploading, uploadComplete;
	private volatile String uploadId;
    
    public CmsSession(Request request) {
        super(request);
        IRepositoryServiceProvider repositoryProvider =((IRepositoryServiceProvider)getApplication());
        repositoryService = new AuthRepositoryService();
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
    	repositoryService.setUser(null);
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
    
    public boolean isUploading(){
      return uploading;
    }

    /**
     * Set when the upload thread starts, and reset when the upload ends or
     * fails.
     */
    public void setIsUploading(boolean uploading){
      this.uploading = uploading;
    }

    public boolean isUploadComplete(){
      return uploadComplete;
    }

    /**
     * Set when the upload thread succeeds, and reset when the upload page is
     * reloaded.
     */
    public void setUploadComplete(boolean uploadComplete){
      this.uploadComplete = uploadComplete;
    }

	public IRepositoryService getRepositoryService() {
		return repositoryService;
	}

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String id) {
        this.uploadId = id;
    }

}
