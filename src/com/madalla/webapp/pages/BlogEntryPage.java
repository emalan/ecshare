package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureWebPage;
import com.madalla.webapp.Panels;

public class BlogEntryPage extends AdminPage implements ISecureWebPage {
    private static final long serialVersionUID = 1L;
    
    public BlogEntryPage(final PageParameters parameters){
    	super(parameters);
        add(Panels.blogEntryPanel("adminPanel", parameters));
    }
}
