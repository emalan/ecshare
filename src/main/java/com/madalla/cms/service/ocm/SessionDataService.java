package com.madalla.cms.service.ocm;

import java.io.Serializable;

import org.emalan.cms.bo.AbstractData;
import org.emalan.cms.bo.security.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.service.DataAccessException;
import com.madalla.service.ISessionDataService;


/**
 * Wrapper class that has session scope and adds security validation to the normal Repository
 * service
 *
 * @author Eugene Malan
 *
 */
public class SessionDataService implements ISessionDataService, Serializable {

	private static final long serialVersionUID = 6043048251504290235L;
	private static final Logger log = LoggerFactory.getLogger(SessionDataService.class);

	private UserData user;

	public SessionDataService(){

	}

	public void validateTransaction(AbstractData data) throws DataAccessException{
		if (null == user){
			throw new DataAccessException("Access Denied! User needs to be set in Session for transaction to be authorized.");
		}
	}

	public void logTransaction(AbstractData data){
		log.info("User:" + user.getName() + ", Type:" + data.getClass().getSimpleName());
	}

	public UserData getUser() {
		return user;
	}

	public void setUser(UserData user) {
		this.user = user;
	}

}
