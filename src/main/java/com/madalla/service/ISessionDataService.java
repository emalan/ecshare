package com.madalla.service;

import com.madalla.bo.AbstractData;
import com.madalla.bo.security.UserData;

public interface ISessionDataService {

	UserData getUser();

	void setUser(UserData user);

	void validateTransaction(AbstractData data) throws DataAccessException;

	void logTransaction(AbstractData data);


}
