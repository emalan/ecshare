package com.madalla.webapp.security;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.madalla.bo.security.UserData;

public class PasswordAuthenticator implements IPasswordAuthenticator{
	
	private static final int DELAY = 10;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	private Map<String, UserLoginTracker> users = new HashMap<String, UserLoginTracker>();
	
	/**
	 * @author emalan
	 *
	 */
	private class UserLoginTracker{

		private int count = 0;
		private DateTime dateTimeAdded;
		private UserData userData;
		
		public UserLoginTracker(UserData userData){
			this.userData = userData;
			this.dateTimeAdded = new DateTime();
		}

	}

	public void addUser(String username, UserData userData){
		if (!users.containsKey(username) || 
				(users.containsKey(username) && 
						users.get(username).dateTimeAdded.plusMinutes(DELAY).isBefore(new DateTime()))){
			users.put(userData.getName(), new UserLoginTracker(userData));
		}
	}
	
	public boolean authenticate(String user, char[] password) {
		return authenticate(user, new String(password));
	}

	public boolean authenticate(String username, String password) {
		log.debug("authenticate - username="+username);
		UserLoginTracker user = users.get(username);
		if (user.userData != null){
			log.debug("authenticate - user found.");
			//validate password
			if (StringUtils.isNotEmpty(user.userData.getPassword()) && user.userData.getPassword().equals(password)){
				return true;
			} else {
				user.count++;
				if(user.count > 5){
					try {
						Thread.sleep(10000); //delay 10 sec
					} catch (InterruptedException e){
						
					}
				}
			}
		}
		return false;
	}
	
}
