package com.madalla.cms.service.jcr;

import com.madalla.webapp.security.IAuthenticator;

public class UserAuthenticator implements IAuthenticator {
	
	private String site;
	
	public UserAuthenticator(String site) {
		this.site = site;
	}

	public boolean authenticate(String user, char[] password) {
		return authenticate(user, password);
	}

	public boolean authenticate(String user, String password) {
		if (user.equalsIgnoreCase("guest")){
			if (password.equals("password")){
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
