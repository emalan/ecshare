package com.madalla.cms.bo.impl.ocm.email;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.bo.email.EmailData;

@Node
public class Email extends EmailData  {

	private static final long serialVersionUID = 1L;
	
	@Field(path=true) private String id;
	
	public Email(){
		
	}

	public Email(String parentPath, String name){
		id = parentPath + "/" + name;
	}
	public String getName(){
		return StringUtils.substringAfterLast(getId(), "/");
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
}
