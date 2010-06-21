package com.madalla.cms.bo.impl.ocm.security;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.bo.security.IProfile;
import com.madalla.bo.security.ProfileData;

@Node
public class Profile extends ProfileData implements IProfile{

	private static final long serialVersionUID = 1L;

	@Field(path=true) private String id;
	@Field private String identifier;
	@Field private String providerName;
	@Field private String displayName;
	@Field private String preferredUsername;
	@Field private String email;
	@Field private String birthday;
	@Field private String utcOffset;
	
	public Profile(){
		
	}
	
	public Profile(String parentPath, String name){
		id = parentPath + "/" + name;
	}
	
	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
	
	@Override
	public String getName() {
		return StringUtils.substringAfterLast(getId(), "/");
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPreferredUsername() {
		return preferredUsername;
	}

	public void setPreferredUsername(String preferredUsername) {
		this.preferredUsername = preferredUsername;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getUtcOffset() {
		return utcOffset;
	}

	public void setUtcOffset(String utcOffset) {
		this.utcOffset = utcOffset;
	}

}
