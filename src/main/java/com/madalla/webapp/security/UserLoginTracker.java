package com.madalla.webapp.security;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.emalan.cms.bo.security.IUserValidate;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserLoginTracker implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(UserLoginTracker.class);
	
	private int count = 0;
	public final DateTime dateTimeAdded;
	public final IUserValidate validate;
	private final int attempts;
	private final int deplay;

	public UserLoginTracker(final IUserValidate validate) {
		this(validate, 10, 10000);
		
	}
	
	public UserLoginTracker(IUserValidate validate, int attempts, int delay) {
		this.validate = validate;
		this.dateTimeAdded = new DateTime();
		this.attempts = attempts;
		this.deplay = delay;
	}

	public boolean validate(final String name, final String password) {
		if (validate != null) {
		log.debug("tracking user: " + name);
		//validate password - non-existant user with Null password will always fail
		if (StringUtils.isNotEmpty(validate.getPassword()) && validate.getPassword().equals(password)){
			count = 0; //reset on successful login
			return true;
		} else {
			log.info("password authenticate failed :" + name + ",count:" + count);
			count++;
			if(count > attempts){
				try {
					Thread.sleep(deplay);
				} catch (InterruptedException e){

				}
			}
		}
		} 
		return false;
	}
	
	public boolean isAttemptCountExceeded() {
		return count > attempts ;
	}
	
	public int getCount() {
		return count;
	}
}
