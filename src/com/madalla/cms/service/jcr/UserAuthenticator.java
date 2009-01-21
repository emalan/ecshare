package com.madalla.cms.service.jcr;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.security.IAuthenticator;

public class UserAuthenticator implements IAuthenticator {
	private static final Log log = LogFactory.getLog(UserAuthenticator.class);
	
	private String site;
	
	public UserAuthenticator(String site) {
		this.site = site;
	}

	public boolean authenticate(String user, char[] password) {
		return authenticate(user, password);
	}

	public boolean authenticate(String user, String password) {
		if (user.equalsIgnoreCase("guest")){
			log.debug("authenticate - password="+password);
			String hash = SecurityUtils.encrypt("password");
			log.debug("authenticate - compare="+hash);
			if (password.equals(hash)){
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
