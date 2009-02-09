package com.madalla.webapp.user;

import java.util.Map;

import com.madalla.bo.security.UserData;
import com.madalla.cms.bo.impl.ocm.security.UserSite;

public class UserDataView extends UserData {
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private Boolean admin;

	public Boolean getAdmin() {
		return admin;
	}
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setApps(Map<String, UserSite> apps) {
		// TODO Auto-generated method stub
		
	}
	public Map<String, UserSite> getApps() {
		// TODO Auto-generated method stub
		return null;
	}

}
