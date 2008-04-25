package com.madalla.service.cms;

import java.util.Locale;

import javax.jcr.RepositoryException;

import com.madalla.webapp.cms.Content;


public interface IContentService {
	
	final String CONTENT = "content";
	final String CONTENT_DEFAULT = "Content";

    public abstract String getContentData(String className, String id);
    public void setContent(Content content) throws RepositoryException;
    public String getLocaleId(String id ,Locale locale);

}