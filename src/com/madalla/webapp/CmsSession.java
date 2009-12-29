package com.madalla.webapp;

import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.Page;
import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

import com.madalla.bo.security.UserData;
import com.madalla.cms.service.ocm.SessionDataService;
import com.madalla.service.IDataService;
import com.madalla.service.IDataServiceProvider;
import com.madalla.service.ISessionDataService;
import com.madalla.service.ISessionDataServiceProvider;
import com.madalla.webapp.cms.IContentAdmin;
import com.madalla.webapp.security.IPasswordAuthenticator;
import com.madalla.webapp.upload.FileUploadGroup;
import com.madalla.webapp.upload.FileUploadStore;
import com.madalla.webapp.upload.IFileUploadInfo;
import com.madalla.webapp.upload.IFileUploadStatus;

public class CmsSession  extends WebSession implements IContentAdmin, ISessionDataServiceProvider, IFileUploadInfo{

	private static final long serialVersionUID = 652426659740076486L;
	private boolean cmsAdminMode = false;
	private String username = null;
	private ISessionDataService repositoryService;
	private Class<? extends Page> adminReturnPage;

	private volatile FileUploadStore fileUploadInfo = new FileUploadStore();
    
    public CmsSession(Request request) {
        super(request);
        this.repositoryService = new SessionDataService();
    }

    public boolean isCmsAdminMode() {
        return cmsAdminMode;
    }
    
    public boolean isSuperAdmin() {
    	return username != null && username.equalsIgnoreCase("admin");
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
    
    public void login(){
    	if (getApplication().getConfigurationType().equals(Application.DEPLOYMENT)){
    		throw new RuntimeException("this Login method only available for DEVELOPMENT environment.");
    	}
    	IDataService service = ((IDataServiceProvider) getApplication()).getRepositoryService();
    	UserData user = service.getUser("admin");
    	repositoryService.setUser(user);
    	cmsAdminMode = true;
    	this.username = "admin";
    	
    }
    
    public boolean login(String userName, String password) {
    	IDataService service = ((IDataServiceProvider) getApplication()).getRepositoryService();
        IPasswordAuthenticator authenticator = service.getPasswordAuthenticator(userName);
        if (authenticator.authenticate(userName, password)){
        	UserData user = service.getUser(userName);
        	if (service.isUserSite(user)){
            	//store user data in session
            	repositoryService.setUser(user);
                cmsAdminMode = (user.getAdmin()==null) ? false : user.getAdmin() ;
            	this.username = userName;
                return true;
        	}
        }
        return false;
    }
    
	public ISessionDataService getRepositoryService() {
		return repositoryService;
	}

	public IFileUploadStatus getFileUploadStatus(String id) {
		return fileUploadInfo.getFileUploadStatus(id);
	}

	public void setFileUploadStatus(String id, IFileUploadStatus status) {
		fileUploadInfo.setFileUploadStatus(id, status);
	}

	public void setFileUploadComplete(String id) {
		fileUploadInfo.setFileUploadComplete(id);
	}
	
	public List<String> getFileUploadStatus(FileUploadGroup group) {
		return fileUploadInfo.getFileUploadStatus(group);
	}

	public void setFileUploadStatus(String id, FileUploadGroup group, IFileUploadStatus status) {
		fileUploadInfo.setFileUploadStatus(id, group, status);
	}

	public void setGroupUploadComplete(FileUploadGroup group) {
		fileUploadInfo.setGroupUploadComplete(group);
	}

	public void setAdminReturnPage(Class<? extends Page> adminReturnPage) {
		this.adminReturnPage = adminReturnPage;
	}

	public Class<? extends Page> getAdminReturnPage() {
		return adminReturnPage;
	}



}
