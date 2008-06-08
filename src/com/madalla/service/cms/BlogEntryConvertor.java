package com.madalla.service.cms;

import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import com.madalla.service.blog.BlogEntry;

public class BlogEntryConvertor implements IContentData {
    
    public static void populateBlogEntry(Node node, BlogEntry blogEntry) throws ValueFormatException, PathNotFoundException, RepositoryException{
        blogEntry.setId(node.getUUID());
        blogEntry.setBlogCategory(node.getProperty(EC_PROP_CATEGORY).getString());
        blogEntry.setDate(node.getProperty(EC_PROP_ENTRYDATE).getDate().getTime());
        blogEntry.setDescription(node.getProperty(EC_PROP_DESCRIPTION).getString());
        blogEntry.setKeywords(node.getProperty(EC_PROP_KEYWORDS).getString());
        blogEntry.setText(node.getProperty(EC_PROP_CONTENT).getString());
        blogEntry.setTitle(node.getProperty(EC_PROP_TITLE).getString());
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
