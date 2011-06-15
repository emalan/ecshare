package com.madalla.bo.security;

import java.io.Serializable;


public interface IUser extends Serializable{

	String getName();

	String getId();

	String getPassword();

	String getEmail();

	void setPassword(String password);

	void setEmail(String email);

	String getFirstName();

	void setFirstName(String firstName);

	String getLastName();

	void setLastName(String lastName);

	Boolean getAdmin();

	void setAdmin(Boolean admin);

	String getDisplayName();

	void setDisplayName(String displayName);

}
