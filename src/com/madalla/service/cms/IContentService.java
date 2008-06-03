package com.madalla.service.cms;

import java.util.Locale;

import javax.jcr.RepositoryException;
import javax.swing.tree.TreeModel;

import com.madalla.webapp.cms.Content;


public interface IContentService {
	
	final String CONTENT_DEFAULT = "Content";

    public String getContentData(String nodeName, String id);
    public String getContentData(String nodeName, String id, Locale locale);
    public void setContent(Content content) throws RepositoryException;
    public void setContent(Content content, Locale locale) throws RepositoryException;

}