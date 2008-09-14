package com.madalla.service.cms;

import java.util.Calendar;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.joda.time.DateTime;
import org.springmodules.jcr.JcrTemplate;

public class BlogEntryHelper extends AbstractContentHelper{
	
	//Repository data names
	static final String EC_NODE_BLOGS = NS + "blogs";
    static final String EC_PROP_TITLE = NS + "title";
    static final String EC_PROP_KEYWORDS = NS + "keywords";
    static final String EC_PROP_DESCRIPTION = NS + "description";
    static final String EC_PROP_ENTRYDATE = NS + "entryDate";
    static final String EC_PROP_CATEGORY = NS + "category";


	private static BlogEntryHelper instance;
	public static BlogEntryHelper getInstance(){
		return instance;
	}

	public BlogEntryHelper(String site, JcrTemplate template ){
		this.site = site;
		this.template = template;
		instance = this;
	}
	
	public String save(final BlogEntry blogEntry) {
		return genericSave(blogEntry);
    }
	
    public static boolean isBlogNode(final String path){
    	String[] pathArray = path.split("/");
    	if (EC_NODE_BLOGS.equals(pathArray[pathArray.length-2])){
    		return true;
    	}
    	return false;
    }
    
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

    @Override
	Node getParentNode(Node node) throws RepositoryException {
		return getCreateNode(EC_NODE_BLOGS, node);
	}

	@Override
	void setPropertyValues(Node node, IRepositoryData data) throws  RepositoryException {
		BlogEntry blogEntry = (BlogEntry) data;
		node.setProperty(EC_PROP_TITLE,blogEntry.getTitle() );
	    node.setProperty(EC_PROP_DESCRIPTION,blogEntry.getDescription() );
	    node.setProperty(EC_PROP_CATEGORY, blogEntry.getCategory());
	    node.setProperty(EC_PROP_KEYWORDS,blogEntry.getKeywords() );
	    node.setProperty(EC_PROP_ENTRYDATE, blogEntry.getDate().toGregorianCalendar());
	    node.setProperty(EC_PROP_CONTENT, blogEntry.getText());
	}

}
