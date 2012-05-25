package com.madalla.service.user;

import org.emalan.cms.IDataService;
import org.emalan.cms.bo.impl.ocm.security.UserSite;
import org.emalan.cms.bo.security.IUserValidate;
import org.emalan.cms.bo.security.UserData;
import org.emalan.cms.bo.security.UserSiteData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.webapp.security.IPasswordAuthenticator;
import com.madalla.webapp.security.PasswordAuthenticator;

public class UserSecurityService {

	private static final Logger log = LoggerFactory.getLogger(UserSecurityService.class);
	private static final Marker fatal = MarkerFactory.getMarker("FATAL");
	
	private PasswordAuthenticator authenticator;
	private IDataService dataService;
	private String site ;
	
    public void init() {
        if (dataService == null) {
            log.error(fatal, "Build Information not configured Correctly.");
            throw new IllegalStateException("Build Information not configured Correctly.");
        }

        //Create default Users if they don't exist yet
        dataService.getNewUser("guest", SecurityUtils.encrypt("password"));
        UserData adminUser = dataService.getNewUser("admin", SecurityUtils.encrypt("password"));
        if (adminUser == null){
            adminUser = dataService.getUser("admin");
        } else {
            dataService.saveUserSite(new UserSite(adminUser.getId(), site));
        }
        adminUser.setAdmin(true);
        dataService.saveDataObject(adminUser);
 
    }

	public IAuthenticator getUserAuthenticator() {
		return new IAuthenticator(){

			public boolean authenticate(String username){
				return dataService.isUserExists(username);
			}

			public boolean requiresSecureAuthentication(String username) {
				if ("admin".equalsIgnoreCase(username)){
					return true;
				}

				if (dataService.isUserExists(username)){
					UserData user = dataService.getUser(username);
					UserSiteData userSite= dataService.getUserSite(user);
					return Boolean.TRUE.equals(userSite.getRequiresAuthentication());
				}
				return false;
			}

		};
	}

	public IPasswordAuthenticator getPasswordAuthenticator(final String username){
		if (username == null){
			throw new IllegalArgumentException("Username Argument may not be null");
		}
		final IUserValidate userData;
		if (dataService.isUserExists(username)){
			userData = dataService.getUser(username);
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

	public void setAuthenticator(final PasswordAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

    public void setDataService(final IDataService dataService) {
        this.dataService = dataService;
    }
    
    public void setSite(final String site) {
        this.site = site;
    }

}
