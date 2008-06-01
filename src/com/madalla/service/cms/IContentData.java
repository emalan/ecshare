package com.madalla.service.cms;

public interface IContentData {
    static final String NS = "ec:";
    static final String EC_NODE_APP = NS + "apps";
    static final String EC_NODE_SITE = NS + "site";
    static final String EC_NODE_PAGE = NS + "page";
    static final String EC_NODE_BLOG = NS + "blog";
    static final String EC_NODE_CONTENTENTRY = NS + "contentEntry";
    static final String EC_NODE_BLOGENTRY = NS + "blogEntry";
    static final String EC_PROP_TITLE = NS + "title";
	static final String EC_PROP_CONTENT = NS + "content";
    static final String EC_PROP_KEYWORDS = NS + "keywords";
    static final String EC_PROP_DESCRIPTION = NS + "description";
    static final String EC_PROP_ENTRYDATE = NS + "entryDate";
    static final String EC_PROP_CATEGORY = NS + "category";
}
