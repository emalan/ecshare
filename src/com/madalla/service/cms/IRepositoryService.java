package com.madalla.service.cms;

import java.util.List;
import java.util.Locale;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

//TODO split into UI public service and package repository util service
public interface IRepositoryService {

	//package methods
	String getSite();
    String getLocaleId(String id, Locale locale);
    Node getApplicationNode(Session session) throws RepositoryException;
    Node getSiteNode(Session session) throws RepositoryException;
    Node getCreateNode(String nodeName, Node parent) throws RepositoryException;
    
    //public
	boolean isDeletableNode(final String path);
	boolean isContentNode(final String path);
    boolean isBlogNode(final String path);
    boolean isContentPasteNode(final String path);
    String getContentData(String nodeName, String id);
    String getContentData(final String nodeName, final String id, Locale locale);
    void pasteContent(final String path, final Content content);
    void setContent(Content content) throws RepositoryException;
    String insertBlogEntry(BlogEntry blogEntry);
    void updateBlogEntry(BlogEntry blogEntry);
    BlogEntry getBlogEntry(final String uuid);
    void deleteNode(final String path);
    Content getContent(final String path);
    List<BlogEntry> getBlogEntries(final String blog);

}
