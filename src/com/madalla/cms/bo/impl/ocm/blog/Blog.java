package com.madalla.cms.bo.impl.ocm.blog;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.bo.blog.BlogData;

@Node
public class Blog extends BlogData{
	private static final long serialVersionUID = 1L;

	@Field(path=true) private String id;
	@Field private String title;
	@Field private String keywords;
	@Field private String description;
	
	public Blog(){
		
	}
	
	public Blog(String parentPath, String blogName){
		id = parentPath + "/" + blogName;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		
	}
}
