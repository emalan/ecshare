package com.madalla.cms.bo.impl.ocm.security;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.bo.security.UserData;

@Node
public class User extends UserData {

	private static final long serialVersionUID = -1983898176405123462L;

	@Field(path=true) private String id;
	@Field private String password;
	@Field private String email;
	
	public User(){
		
	}

	public User(String parentPath, String name){
		id = parentPath + "/" + name;
	}
	
	public String getName(){
	    return StringUtils.substringAfterLast(getId(), "/");
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
