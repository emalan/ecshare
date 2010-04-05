package com.madalla.util.security;

import java.io.Serializable;

public interface ICredentialHolder extends Serializable{

	String getUsername();
	String getPassword();
	ICredentialHolder setUsername(String username);
	ICredentialHolder setPassword(String password);
}
