package com.madalla.webapp.security;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private Map<String, UserLoginTracker> users = new HashMap<String, UserLoginTracker>();
	
	/**
	 * @author Eugene Malan
	 *
	 */
	public class UserLoginTracker implements Serializable{
		private static final long serialVersionUID = 1L;

		public int count = 0;
		private final DateTime dateTimeAdded;
		private final IUserValidate userData;
		
		public UserLoginTracker(IUserValidate userData){
			this.userData = userData;
			this.dateTimeAdded = new DateTime();
		}

	}

	public void addUser(String username, IUserValidate userData){
		if (!users.containsKey(username) || 
				(users.containsKey(username) && 
						users.get(username).dateTimeAdded.plusMinutes(REFRESH).isBefore(new DateTime()))){
			users.put(userData.getName(), new UserLoginTracker(userData));
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
		UserLoginTracker user = users.get(username);
		if (user == null){
			throw new RuntimeException("Authenticate failed. User not being tracked - " + username);
		}
		if (user.userData != null){
			log.debug("tracking user :" + username);
			//validate password - non-existant user with Null password will always fail
			if (StringUtils.isNotEmpty(user.userData.getPassword()) && user.userData.getPassword().equals(password)){
				user.count = 0; //reset on successful login
				return true;
			} else {
				log.debug("password authenticate failed :" + username + ",count:"+user.count);
				user.count++;
				if(user.count > ATTEMPTS){
					try {
						Thread.sleep(DELAY);
					} catch (InterruptedException e){
						
					}
				}
			}
		}
		return false;
	}
	
}
