package com.madalla.webapp.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.joda.time.DateTime;

import com.madalla.bo.security.IUserValidate;

/**
 * Authenticator with user simple login tracker.
 *
 * Will keep track of failed logins and add a delay after a number of false ones
 *
 * @author Eugene Malan
 *
 */
public class PasswordAuthenticator implements IPasswordAuthenticator, Serializable{
	private static final long serialVersionUID = 5844968243319652152L;

	private static final int REFRESH = 10; //refresh user after so many minutes
	private static final int ATTEMPTS = 5; //after so many attempts implement deplay
	private static final int DELAY = 10000; // delay in milliseconds

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private Map<String, UserLoginTracker> users = new HashMap<String, UserLoginTracker>();



	public void clearUser(String username){
		users.remove(username);
	}

	public void addUser(String username, IUserValidate userData){
		if (!users.containsKey(username) ||
				(users.containsKey(username) &&
						users.get(username).dateTimeAdded.plusMinutes(REFRESH).isBefore(new DateTime()))){
			users.put(userData.getName(), new UserLoginTracker(userData, ATTEMPTS, DELAY));
		}
	}

	public UserLoginTracker getUserLoginTracker(String user){
		return users.get(user);
	}

	public boolean authenticate(String user, char[] password) {
		return authenticate(user, new String(password));
	}

	public boolean authenticate(String username, String password) {
		log.debug("password authenticate - username="+username);
		UserLoginTracker userTracker = users.get(username);
		if (userTracker == null){
			throw new RuntimeException("Authenticate failed. User not being tracked - " + username);
		}
		return userTracker.validate(username, password);
	}

}
