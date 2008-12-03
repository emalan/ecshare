package com.madalla.service.cms.ocm.blog;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.service.cms.AbstractBlog;

@Node
public class Blog extends AbstractBlog{
	private static final long serialVersionUID = 1L;

	@Field(path=true) private String id;
	@Field private String title;
	@Field private String keywords;
	@Field private String description;
	
	public Blog(){
		
	}
	
	public Blog(String path){
		id = path;
	}
	
	public Blog(String parentPath, String blogName){
		id = parentPath + "/" + blogName;
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
