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
	@Field private String username;

	public User(String parentPath, String name){
		id = parentPath + "/" + name;
	}
	
	public String getName(){
	    return StringUtils.substringAfterLast(getId(), "/");
	}
	
	public String getId() {
		return id;
	}

	public String getPassword() {
		return password;
	}

	public String getUsername() {
		return username;
	}

}
