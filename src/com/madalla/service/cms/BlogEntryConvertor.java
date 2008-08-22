package com.madalla.service.cms;

import java.util.Calendar;
import java.util.Date;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import com.madalla.service.blog.BlogEntry;

public class BlogEntryConvertor implements IContentData {
    
    public static BlogEntry createBlogEntry(Node node) throws ValueFormatException, PathNotFoundException, RepositoryException{
    	String blog = node.getParent().getName().replaceFirst(NS,"");
    	String category = node.getProperty(EC_PROP_CATEGORY).getString();
    	String title = node.getProperty(EC_PROP_TITLE).getString();
    	Date date = node.getProperty(EC_PROP_ENTRYDATE).getDate().getTime();
    	
    	BlogEntry blogEntry = new BlogEntry(blog, category, date, title );
    	blogEntry.setBlog(blog);
        blogEntry.setId(node.getPath());
        blogEntry.setDescription(node.getProperty(EC_PROP_DESCRIPTION).getString());
        blogEntry.setKeywords(node.getProperty(EC_PROP_KEYWORDS).getString());
        blogEntry.setText(node.getProperty(EC_PROP_CONTENT).getString());
        return blogEntry;
    }
    
    public static void populateNode(Node node, BlogEntry blogEntry) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException{
        node.setProperty(EC_PROP_CONTENT, blogEntry.getText());
        node.setProperty(EC_PROP_CATEGORY, blogEntry.getBlogCategory());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(blogEntry.getDate());
        node.setProperty(EC_PROP_ENTRYDATE, calendar );
        node.setProperty(EC_PROP_DESCRIPTION,blogEntry.getDescription() );
        node.setProperty(EC_PROP_KEYWORDS,blogEntry.getKeywords() );
        node.setProperty(EC_PROP_TITLE,blogEntry.getTitle() );
    }

}
