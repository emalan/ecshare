package com.madalla.service.cms;

import javax.jcr.RepositoryException;

import com.madalla.webapp.cms.Content;


public interface IContentService {
	
	final String CONTENT = "content";
    final String CONTENT_DEFAULT = "Content in process of being created.";

	void loadContent(String className);
    public abstract String getContentData(String className, String id);
    public void setContent(Content content) throws RepositoryException;

}