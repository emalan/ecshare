package com.madalla.webapp.user;

import com.madalla.bo.security.UserData;

public class UserDataView extends UserData {
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private Boolean admin;
	private Boolean requiresAuth;
	private String displayName;

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
	public void setRequiresAuth(Boolean requiresAuth) {
		this.requiresAuth = requiresAuth;
	}
	public Boolean getRequiresAuth() {
		return requiresAuth;
	}
	
	public void clear(){
		id = "";
		name = "";
		email = "";
		firstName = "";
		lastName = "";
		password = "";
		admin = null;
		requiresAuth = null;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
		
	}

}
