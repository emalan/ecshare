package com.madalla.service.cms;


public abstract class AbstractBlog {
	
	public abstract String getId();
	public abstract String getTitle();
	public abstract void setTitle(String title);
	public abstract String getKeywords();
	public abstract void setKeywords(String keywords);
	public abstract String getDescription();
	public abstract void setDescription(String description);
	
}
