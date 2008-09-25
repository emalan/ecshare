package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.cms.admin.ContentAdminPanel;

public class ContentAdminPage extends AdminPage  {
	private static final long serialVersionUID = -2835770167598542155L;
    
    public ContentAdminPage(final PageParameters parameters){
        add(ContentAdminPanel.newInstance("contentAdminPanel", parameters));
    }

}