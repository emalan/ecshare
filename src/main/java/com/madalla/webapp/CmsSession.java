package com.madalla.webapp;

import java.util.List;

import org.apache.wicket.Application;
import org.apache.wicket.Request;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.session.pagemap.IPageMapEntry;

import com.madalla.bo.security.UserData;
import com.madalla.bo.security.UserSiteData;
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

public class CmsSession  extends AuthenticatedWebSession implements IContentAdmin, ISessionDataServiceProvider, IFileUploadInfo{

	private static final long serialVersionUID = 652426659740076486L;
	public final static String SUPERADMIN = "SUPERADMIN";
	public final static String SECURE = "SECURE";
	public final static String CONTENTADMIN = "CONTENTADMIN";
	
	private Roles roles;
	private ISessionDataService repositoryService;
	private IPageMapEntry lastSitePage;

	private volatile FileUploadStore fileUploadInfo = new FileUploadStore();
    
    public CmsSession(Request request) {
        super(request);
        this.repositoryService = new SessionDataService();
    }

    public boolean isCmsAdminMode() {
        return getRoles().hasRole(Roles.ADMIN);
    }
    
    public boolean isSuperAdmin() {
    	return getRoles().hasRole(SUPERADMIN);
    }
    
    @Override
	public void signOut() {
		super.signOut();
		roles.clear();
    	repositoryService.setUser(null);
    	fileUploadInfo.clear();
	}

	public void authenticate(){
		if (getApplication().getConfigurationType().equals(Application.DEPLOYMENT)){
    		throw new RuntimeException("this Login method only available for DEVELOPMENT environment.");
    	}
    	IDataService service = ((IDataServiceProvider) getApplication()).getRepositoryService();
    	UserData user = service.getUser("admin");
    	repositoryService.setUser(user);
    	setRoles(user);
	}
    
	@Override
	public boolean authenticate(String userName, String password) {
	   	IDataService service = ((IDataServiceProvider) getApplication()).getRepositoryService();
        IPasswordAuthenticator authenticator = service.getPasswordAuthenticator(userName);
        if (authenticator.authenticate(userName, password)){
        	UserData user = service.getUser(userName);
        	
        	if (service.isUserSite(user)){
            	//store user data in session
            	repositoryService.setUser(user);
            	setRoles(user);
                
                return true;
        	}
        }
        return false;
	}
	
	private void setRoles(UserData user){
		StringBuilder sb = new StringBuilder(Roles.USER);
		if ((user.getAdmin()==null) ? false : user.getAdmin()){
			sb.append(", " + Roles.ADMIN);
		}
		if (user.getName().equalsIgnoreCase("admin")){
			sb.append(", " + SUPERADMIN + ", " + SECURE);
		}
		IDataService service = ((IDataServiceProvider) getApplication()).getRepositoryService();
		UserSiteData userSite = service.getUserSite(user);
		if (Boolean.TRUE.equals(userSite.getRequiresAuthentication())){
			sb.append(", " + SECURE);
		}

		roles = new Roles(sb.toString());
		
	}

	@Override
	public Roles getRoles() {
		return roles == null? new Roles() : roles;
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

	public boolean isLoggedIn() {
		return isSignedIn();
	}

	public void setLastSitePage(IPageMapEntry lastSitePage) {
		this.lastSitePage = lastSitePage;
	}

	public IPageMapEntry getLastSitePage() {
		return lastSitePage ;
	}
	


}
