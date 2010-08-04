package com.madalla.cms.service.ocm;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.madalla.bo.AbstractData;
import com.madalla.bo.security.IUser;
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
	private static final Log log = LogFactory.getLog(SessionDataService.class);
	
	private IUser user;
	
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
	
	public IUser getUser() {
		return user;
	}

	public void setUser(IUser user) {
		this.user = user;
	}

}