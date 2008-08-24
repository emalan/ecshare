package com.madalla.service.blog;

import java.util.Calendar;
import java.util.Date;

import javax.jcr.Node;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import com.madalla.service.cms.IContentData;

/**
 * Converts Node to BlogEntry and visa versa
 * 
 * @author emalan
 *
 */
public class BlogEntryConvertor implements IContentData {
    
    public static BlogEntry createBlogEntry(Node node) throws ValueFormatException, PathNotFoundException, RepositoryException{
    	String blog = node.getParent().getName().replaceFirst(NS,"");
    	String title = node.getProperty(EC_PROP_TITLE).getString();
    	Date date = node.getProperty(EC_PROP_ENTRYDATE).getDate().getTime();
    	String category = node.getProperty(EC_PROP_CATEGORY).getString();
    	String description = node.getProperty(EC_PROP_DESCRIPTION).getString();
    	String keywords = node.getProperty(EC_PROP_KEYWORDS).getString();
    	String text = node.getProperty(EC_PROP_CONTENT).getString();
    	
    	return new BlogEntry.Builder(node.getPath(), blog, title, date).
    	category(category).desription(description).keywords(keywords).text(text).build();
    }
    
    public static void populateNode(Node node, BlogEntry blogEntry) throws ValueFormatException, VersionException, LockException, ConstraintViolationException, RepositoryException{
        node.setProperty(EC_PROP_CONTENT, blogEntry.getText());
        node.setProperty(EC_PROP_CATEGORY, blogEntry.getCategory());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(blogEntry.getDate());
        node.setProperty(EC_PROP_ENTRYDATE, calendar );
        node.setProperty(EC_PROP_DESCRIPTION,blogEntry.getDescription() );
        node.setProperty(EC_PROP_KEYWORDS,blogEntry.getKeywords() );
        node.setProperty(EC_PROP_TITLE,blogEntry.getTitle() );
    }

}
