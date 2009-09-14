package com.madalla.util.security;

import java.io.Serializable;

public class SecureCredentials implements ICredentialHolder, Serializable{

	private static final long serialVersionUID = 461686839345197059L;

	private String password;
	private String username;
	
	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		//encrypt password
		this.password = SecurityUtils.encrypt(password);
	}

	public ICredentialHolder setUsername(String username) {
		this.username = username;
		return this;
	}

}
