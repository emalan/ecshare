package com.madalla.webapp.pages;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureWebPage;
import com.madalla.webapp.blog.admin.BlogEntryPanel;

public class BlogEntryPage extends AdminPage implements ISecureWebPage {
    private static final long serialVersionUID = 1L;
    
    public BlogEntryPage(String blogName){
    	super();
    	add(new BlogEntryPanel("adminPanel", blogName) );
    }
    
    public BlogEntryPage(String blogName, String blogEntryId){
    	super();
    	add(new BlogEntryPanel("adminPanel", blogName, blogEntryId));
    }
}
