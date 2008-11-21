package com.madalla.service.cms.ocm;

import java.util.Map;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.service.cms.ocm.blog.Blog;
import com.madalla.service.cms.ocm.image.Album;
import com.madalla.service.cms.ocm.page.Page;

@Node
public class Site {

	@Field(path=true) private String path;
	@Collection private Map<String, Blog> pages;
	@Collection private Map<String, Album> albums;
	@Collection private Map<String, Page> blogs;
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Map<String, Blog> getPages() {
		return pages;
	}
	public void setPages(Map<String, Blog> pages) {
		this.pages = pages;
	}
	public Map<String, Page> getBlogs() {
		return blogs;
	}
	public void setBlogs(Map<String, Page> blogs) {
		this.blogs = blogs;
	}
	public Map<String, Album> getAlbums() {
		return albums;
	}
	public void setAlbums(Map<String, Album> albums) {
		this.albums = albums;
	}
	
}
