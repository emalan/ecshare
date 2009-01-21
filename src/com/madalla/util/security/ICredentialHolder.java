package com.madalla.util.security;

public interface ICredentialHolder {

	String getUsername();
	String getPassword();
	void setUsername(String username);
	void setPassword(String password);
}
