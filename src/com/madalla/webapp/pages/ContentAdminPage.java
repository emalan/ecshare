package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureAdminPage;
import com.madalla.webapp.Panels;

public class ContentAdminPage extends AdminPage implements ISecureAdminPage  {
    
    public ContentAdminPage(final PageParameters parameters){
    	super(parameters);
        add(Panels.contentAdminPanelForSite("adminPanel"));
    }

}