package com.madalla.util.security;

public interface ICredentialHolder {

	String getUsername();
	String getPassword();
	ICredentialHolder setUsername(String username);
	void setPassword(String password);
}
