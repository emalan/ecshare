package com.madalla.service;

import org.emalan.cms.bo.AbstractData;
import org.emalan.cms.bo.security.UserData;

public interface ISessionDataService {

	UserData getUser();

	void setUser(UserData user);

	void validateTransaction(AbstractData data) throws DataAccessException;

	void logTransaction(AbstractData data);


}
