package com.madalla.service.cms;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;


public abstract class AbstractBlog implements Serializable{
	
	private static final long serialVersionUID = 1L;

	public String getName(){
		return StringUtils.substringAfterLast(getId(), "/");
	}
	public abstract String getId();
	public abstract String getTitle();
	public abstract void setTitle(String title);
	public abstract String getKeywords();
	public abstract void setKeywords(String keywords);
	public abstract String getDescription();
	public abstract void setDescription(String description);
	
}
