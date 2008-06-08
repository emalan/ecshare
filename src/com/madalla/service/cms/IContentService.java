package com.madalla.service.cms;

import java.util.List;
import java.util.Locale;

import javax.jcr.RepositoryException;

import com.madalla.service.blog.BlogEntry;
import com.madalla.webapp.cms.Content;


public interface IContentService {
	
	final String CONTENT_DEFAULT = "Content";

    String getContentData(String nodeName, String id);
    String getContentData(String nodeName, String id, Locale locale);
    void setContent(Content content) throws RepositoryException;
    void setContent(Content content, Locale locale) throws RepositoryException;
    String insertBlogEntry(BlogEntry blogEntry);
    void updateBlogEntry(BlogEntry blogEntry);
    BlogEntry getBlogEntry(final String uuid);
    void deleteBlogEntry(final String uuid);
    public List getBlogEntries(final String blog);
}