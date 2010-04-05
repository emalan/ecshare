package com.madalla.service;

import com.madalla.bo.AbstractData;
import com.madalla.bo.security.IUser;

public interface ISessionDataService {
	
	IUser getUser();

	void setUser(IUser user);
	
	void validateTransaction(AbstractData data) throws DataAccessException;
	
	void logTransaction(AbstractData data);


}
