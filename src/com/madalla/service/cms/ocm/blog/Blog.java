package com.madalla.service.cms.ocm.blog;

import java.util.Map;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.service.cms.AbstractBlog;

@Node()
public class Blog extends AbstractBlog {

	@Collection private Map<String, BlogEntry> blogEntry;

	public void setBlogEntry(Map<String, BlogEntry> blogEntry) {
		this.blogEntry = blogEntry;
	}

	public Map<String, BlogEntry> getBlogEntry() {
		return blogEntry;
	}
}
