package com.madalla.service.cms.ocm.blog;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node
public class Blog  {

	@Field(path=true) private String id;
	@Field private String title;
	@Field private String keywords;
	@Field private String description;
	//@Collection private Map<String, BlogEntry> blogEntry;
	
//	public void setBlogEntry(Map<String, BlogEntry> blogEntry) {
//		this.blogEntry = blogEntry;
//	}
//
//	public Map<String, BlogEntry> getBlogEntry() {
//		return blogEntry;
//	}


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
