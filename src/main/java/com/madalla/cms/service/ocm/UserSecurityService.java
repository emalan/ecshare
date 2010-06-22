package com.madalla.cms.service.ocm;

import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.WicketRuntimeException;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.bo.AbstractData;
import com.madalla.bo.SiteData;
import com.madalla.bo.security.IUser;
import com.madalla.bo.security.IUserValidate;
import com.madalla.bo.security.ProfileData;
import com.madalla.bo.security.UserData;
import com.madalla.bo.security.UserSiteData;
import com.madalla.cms.bo.impl.ocm.security.Profile;
import com.madalla.cms.bo.impl.ocm.security.User;
import com.madalla.cms.bo.impl.ocm.security.UserSite;
import com.madalla.cms.service.ocm.RepositoryInfo.RepositoryType;
import com.madalla.cms.service.ocm.template.RepositoryTemplate;
import com.madalla.cms.service.ocm.template.RepositoryTemplateCallback;
import com.madalla.db.dao.TransactionLogDao;
import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.webapp.security.IPasswordAuthenticator;
import com.madalla.webapp.security.PasswordAuthenticator;

public class UserSecurityService extends AbstractRepositoryService{

	private static final Log log = LogFactory.getLog(UserSecurityService.class);

	private PasswordAuthenticator authenticator;
	private List<SiteData> siteEntries;
	
	public void init(List<SiteData> siteEntries, RepositoryTemplate repositoryTemplate){
		
		this.siteEntries = siteEntries;
		this.repositoryTemplate = repositoryTemplate;
	   	
		//Create default Users if they don't exist yet
    	getNewUser("guest", SecurityUtils.encrypt("password"));
    	UserData adminUser = getNewUser("admin", SecurityUtils.encrypt("password"));
    	if (adminUser == null){
    	    adminUser = getUser("admin");
    	} else {
    		saveUserSite(new UserSite(adminUser.getId(), site));
    	}
        adminUser.setAdmin(true);
        saveDataObject(adminUser);
	}
	
	public ProfileData getUserProfile(final String identifier){
		List<ProfileData> list = getUserProfiles();
		for(ProfileData profile: list){
			if (profile.getIdentifier().equals(identifier)){
				return profile;
			}
		}
		return null;
	}
	
	public UserData getUser(final ProfileData profile){
		return (UserData) repositoryTemplate.getParentObject(RepositoryType.USER, profile);
	}
	
	public ProfileData getNewUserProfile(IUser user, String name, String identifier){
		if (user == null){
			throw new WicketRuntimeException("User may not be null");
		}
		ProfileData profile = new Profile(user.getId(), name);
		profile.setIdentifier(identifier);
		return profile;
	}
	
	public UserData getNewUser(String username, String password) {
	   	username = username.toLowerCase();
    	if (isUserExists(username)){
    		return null;
    	}
    	UserData user = getUser(username);
    	user.setPassword(password);
    	saveDataObject(user);
    	return user;
	}
	
	public UserData getUser(String username) {
		username = username.toLowerCase();
    	return (User) repositoryTemplate.getParentObject(RepositoryType.USER, username, new RepositoryTemplateCallback(){

			@Override
			public AbstractData createNew(String parentPath, String name) {
				return new User(parentPath, name);
			}

    	});
	}
	
	@SuppressWarnings("unchecked")
	public List<UserData> getUsers(){
		List<UserData> list = (List<UserData>) repositoryTemplate.getAll(RepositoryType.USER);
		Collections.sort(list);
		return list;
	}
	
	public IAuthenticator getUserAuthenticator() {
		return new IAuthenticator(){
			
			public boolean authenticate(String username){
				return isUserExists(username);
			}
			
			public boolean requiresSecureAuthentication(String username) {
				if ("admin".equalsIgnoreCase(username)){
					return true;
				}
				
				if (isUserExists(username)){
					UserData user = getUser(username);
					UserSiteData userSite= getUserSite(user);
					return Boolean.TRUE.equals(userSite.getRequiresAuthentication());
				}
				return false;
			}

		};
	}

	public IPasswordAuthenticator getPasswordAuthenticator(final String username){
		if (username == null){
			throw new WicketRuntimeException("Username Argument may not be null");
		}
		IUserValidate userData;
		if (isUserExists(username)){
			userData = getUser(username);
		} else {
			userData = new IUserValidate(){

				public String getName() {
					return username;
				}

				public String getPassword() {
					return null;
				}
				
			};
		}
		authenticator.addUser(username, userData);
		return authenticator;
		
	}

	// User Site
	
	public UserSiteData getUserSite(UserData user) {
		return getUserSite(user, site);
	}

	@SuppressWarnings("unchecked") //Unsafe cast
	public List<UserSiteData> getUserSiteEntries(UserData user){
		List<UserSiteData> list = (List<UserSiteData>) repositoryTemplate.getAll(RepositoryType.USERSITE, user);
		Collections.sort(list);
		return list;
	}

	public boolean isUserSite(UserData userData) {
		log.debug("isUserSite - Doing Site validation...");
		List<UserSiteData> sites = getUserSiteEntries(userData);
		for (UserSiteData siteData : sites){
			if (siteData.getName().equals(site)){
				log.debug("authenticate - site validation success.");
				return true;
			}
		}
		if (userData.getName().equals("admin") && site.equals("ecadmin")){
			log.debug("isUserSite - site validation success. Special case for admin user.");
		    return true;
		}
		log.debug("isUserSite - site validation failed!");
		return false;
	}
	
	public void saveUserSite(UserSiteData data){
		saveDataObject(data);
	}
	
	public void saveUserSiteEntries(UserData user, List<SiteData> sites, boolean auth){
		log.debug("saveUserSiteEntries -"+sites);
		
		List<UserSiteData> existingUserSites = getUserSiteEntries(user);
		
		for (SiteData site : siteEntries){
			if (sites.contains(site)){
				UserSiteData userSite = getUserSite(user, site.getName());
				userSite.setRequiresAuthentication(auth);
				saveUserSite(userSite);
				//remove from existing list so we end up with list of ones to delete
				existingUserSites.remove(userSite);
			}
		}
		//delete the rest
		for (UserSiteData userSite : existingUserSites){
			deleteNode(userSite.getId());
		}
	}
	
	//utlity
	@SuppressWarnings("unchecked")
	private List<ProfileData> getUserProfiles(){
		return (List<ProfileData>) repositoryTemplate.getAll(RepositoryType.USERPROFILE);
	}
	
    private boolean isUserExists(String username){
    	return repositoryTemplate.checkExists(RepositoryType.USER, username);
    }

	private UserSiteData getUserSite(UserData user, String site){
		return (UserSiteData) repositoryTemplate.getOcmObject(RepositoryType.USERSITE, user, site, new RepositoryTemplateCallback(){

			@Override
			public AbstractData createNew(String parentPath, String name) {
				return new UserSite(parentPath, name) ;
			}
			
		});
	}

    public void saveDataObject(AbstractData data, String user){
    	createTransactionLog(user, data);
    	saveDataObject(data);
    }
    
    public void saveDataObject(AbstractData data){
    	repositoryTemplate.saveDataObject(data);
    }
    
	public void setAuthenticator(PasswordAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

    public void setTemplate(JcrTemplate template) {
        this.template = template;
    }
    
	public void setSite(String site) {
		this.site = site;
	}
	
	public void setTransactionLogDao(TransactionLogDao transactionLogDao) {
		this.transactionLogDao = transactionLogDao;
	}

}
