package com.madalla.service.cms;

public interface IContentAware {
	//Types
	final static String TYPE_IMAGE = "image";
	final static String TYPE_BLOG = "blog";
	final static String TYPE_TEXT = "text";
	
    static final String NS = "ec:";
    static final String EC_NODE_BACKUP = NS + "backup";
    static final String EC_NODE_APP = NS + "apps";
    static final String EC_NODE_PAGES = NS + "pages";
    static final String EC_NODE_BLOGS = NS + "blogs";
    static final String EC_NODE_IMAGES = NS + "images";
    static final String EC_NODE_CONTENT = NS + "content";
    
    //static final String EC_NODE_CONTENTENTRY = NS + "contentEntry";
    //static final String EC_NODE_BLOGENTRY = NS + "blogEntry";
    
    static final String EC_PROP_TITLE = NS + "title";
	static final String EC_PROP_CONTENT = NS + "content";
    static final String EC_PROP_KEYWORDS = NS + "keywords";
    static final String EC_PROP_DESCRIPTION = NS + "description";
    static final String EC_PROP_ENTRYDATE = NS + "entryDate";
    static final String EC_PROP_CATEGORY = NS + "category";
}
