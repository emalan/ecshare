package com.madalla.webapp;

import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.Request;
import org.apache.wicket.Session;
import org.apache.wicket.authentication.AuthenticatedWebSession;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.apache.wicket.session.pagemap.IPageMapEntry;

import com.madalla.bo.security.IUser;
import com.madalla.bo.security.ProfileData;
import com.madalla.bo.security.UserData;
import com.madalla.bo.security.UserSiteData;
import com.madalla.cms.service.ocm.SessionDataService;
import com.madalla.service.IDataService;
import com.madalla.service.IDataServiceProvider;
import com.madalla.service.ISessionDataService;
import com.madalla.service.ISessionDataServiceProvider;
import com.madalla.webapp.admin.member.MemberSession;
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
	
	public static CmsSession get()
	{
		return (CmsSession)Session.get();
	}
	
	private Roles roles;
	private ISessionDataService repositoryService;
	private IPageMapEntry lastSitePage;
	private MemberSession memberSession = new MemberSession() {

		private static final long serialVersionUID = 1L;

		@Override
		protected boolean authenticateMember(String memberName, String password) {
			IPasswordAuthenticator authenticator = getDataService().getMemberAuthenticator(memberName);
			if (authenticator.authenticate(memberName, password)){
				setMember(getDataService().getMember(memberName));
				return true;
			}
			return false;
		}

	};

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

	/**
	 * Convenience method for automatic login as admin.
	 * NOTE: Make sure this is not used by the deployed application.
	 */
	public void authenticate(){
		if (getApplication().getConfigurationType().equals(Application.DEPLOYMENT)){
    		throw new RuntimeException("this Login method only available for DEVELOPMENT environment.");
    	}
    	UserData user = getDataService().getUser("admin");
    	repositoryService.setUser(user);
    	setRoles(user);
	}
	
	public boolean authenticate(HashMap<String, String> profileData){
		String identifier = profileData.get("identifier");
		String providerName = profileData.get("providerName");
		String preferredUsername = profileData.get("preferredUsername");
		String displayName = profileData.get("displayName");
		
		ProfileData profile = getDataService().getProfile(identifier);

		if (profile == null){
			
			IUser user = null;
			String username = StringUtils.defaultIfEmpty(StringUtils.defaultIfEmpty(preferredUsername, displayName),
					RandomStringUtils.randomAlphabetic(6));
			for (int i = 0; user == null; i++) {
				String uniqueUsername = (i == 0) ? username : username + i;
				user = getDataService().getNewUser(uniqueUsername, "");
			}
			
			profile = getDataService().getNewUserProfile(user, providerName, identifier);
		} 
		
		//update profile data
		profile.setDisplayName(displayName);
		profile.setPreferredUsername(preferredUsername);
		profile.setEmail(profileData.get("email"));
		profile.setBirthday(profileData.get("birthday"));
		profile.setUtcOffset(profileData.get("utcOffset"));
		getDataService().saveDataObject(profile);
		
		signIn(true);
		UserData user = getDataService().getUser(profile);
		if (StringUtils.isEmpty(user.getEmail())){
			user.setEmail(profile.getEmail());
		}
		if (StringUtils.isEmpty(user.getDisplayName())){
			user.setDisplayName(profile.getDisplayName());
		}
		getDataService().saveDataObject(user);
		
		repositoryService.setUser(user);
    	setRoles(user);
		return true;
	}
    
	/* (non-Javadoc)
	 * @see org.apache.wicket.authentication.AuthenticatedWebSession#authenticate(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean authenticate(String userName, String password) {
	   	IDataService service = getDataService();
        IPasswordAuthenticator authenticator = getDataService().getPasswordAuthenticator(userName);
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
	
	public void setUser(IUser user){
		repositoryService.setUser(user);
	}
	
	private void setRoles(UserData user){
		StringBuilder sb = new StringBuilder(Roles.USER);
		if ((user.getAdmin()==null) ? false : user.getAdmin()){
			sb.append(", " + Roles.ADMIN);
		}
		if (user.getName().equalsIgnoreCase("admin")){
			sb.append(", " + SUPERADMIN + ", " + SECURE);
		}
		UserSiteData userSite = getDataService().getUserSite(user);
		if (Boolean.TRUE.equals(userSite.getRequiresAuthentication())){
			sb.append(", " + SECURE);
		}

		roles = new Roles(sb.toString());
		
	}
	
	@Override
	public Roles getRoles() {
		return roles == null? new Roles() : roles;
	}
	
	// Member
	
	public MemberSession getMemberSession(){
		return memberSession;
	}
	
	private IDataService getDataService(){
		return ((IDataServiceProvider) getApplication()).getRepositoryService();
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
