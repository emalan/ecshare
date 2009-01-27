package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureAdminPage;
import com.madalla.webapp.Panels;

public class ContentAdminPage extends AdminPage implements ISecureAdminPage  {
	private static final long serialVersionUID = -2835770167598542155L;
    
    public ContentAdminPage(final PageParameters parameters){
        add(Panels.contentAdminPanelForSite("contentAdminPanel", parameters));
    }

}