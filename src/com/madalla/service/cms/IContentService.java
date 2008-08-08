package com.madalla.service.cms;

import java.util.List;
import java.util.Locale;

import javax.jcr.RepositoryException;

import com.madalla.service.blog.BlogEntry;


public interface IContentService {
	
	final String CONTENT_DEFAULT = "Content";

	boolean isContentNode(String path);
    boolean isBlogNode(String path);
    boolean isContentPastNode(String path);
    String getContentData(String nodeName, String id);
    String getContentData(String nodeName, String id, Locale locale);
    void setContent(Content content) throws RepositoryException;
    void setContent(Content content, Locale locale) throws RepositoryException;
    String insertBlogEntry(BlogEntry blogEntry);
    void updateBlogEntry(BlogEntry blogEntry);
    BlogEntry getBlogEntry(final String uuid);
    void deleteNode(final String path);
    Content getContent(final String path);
    public List getBlogEntries(final String blog);
}