package com.madalla.bo.security;

public interface IUser {

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
}
