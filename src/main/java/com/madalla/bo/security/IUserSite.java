package com.madalla.bo.security;

public interface IUserSite {

	String getName();

	String getId();

	Boolean getRequiresAuthentication();

	void setRequiresAuthentication(Boolean requiresAuthentication);

}
