package com.madalla.service.cms;

import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.joda.time.DateTime;


/**
 * Converts Node to BlogEntry and visa versa
 * 
 * @author emalan
 * TODO turn this into generic convertor class for all types
 */
class BlogEntryConvertor implements IContentAware, IEntryConvertor {
    
    public static BlogEntry createBlogEntry(Node node) throws RepositoryException{
    	String blog = node.getParent().getName().replaceFirst(NS,"");
    	String title = node.getProperty(EC_PROP_TITLE).getString();
    	String description = node.getProperty(EC_PROP_DESCRIPTION).getString();
    	String category = node.getProperty(EC_PROP_CATEGORY).getString();
    	String keywords = node.getProperty(EC_PROP_KEYWORDS).getString();
    	Calendar calendar = node.getProperty(EC_PROP_ENTRYDATE).getDate();
    	String text = node.getProperty(EC_PROP_CONTENT).getString();
    	
    	return new BlogEntry.Builder(node.getPath(), blog, title, new DateTime(calendar)).
    	category(category).desription(description).keywords(keywords).text(text).build();
    }
    
    public void populateNode(Node node, BlogEntry blogEntry) throws RepositoryException{
    	node.setProperty(EC_PROP_TITLE,blogEntry.getTitle() );
        node.setProperty(EC_PROP_DESCRIPTION,blogEntry.getDescription() );
        node.setProperty(EC_PROP_CATEGORY, blogEntry.getCategory());
        node.setProperty(EC_PROP_KEYWORDS,blogEntry.getKeywords() );
        node.setProperty(EC_PROP_ENTRYDATE, blogEntry.getDate().toGregorianCalendar());
        node.setProperty(EC_PROP_CONTENT, blogEntry.getText());
        
    }

	public void populateNode(Node node, IContentData content)
			throws RepositoryException {
		populateNode(node, (BlogEntry) content);
		
	}

}
