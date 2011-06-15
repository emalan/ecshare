package com.madalla.util.security;



public class SecureCredentials implements ICredentialHolder{

	private static final long serialVersionUID = 461686839345197059L;

	private String password;
	private String username;

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

	public ICredentialHolder setPassword(String password) {
		//encrypt password
		this.password = SecurityUtils.encrypt(password);
		return this;
	}

	public ICredentialHolder setUsername(String username) {
		this.username = username;
		return this;
	}

}
