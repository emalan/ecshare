package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureAdminPage;
import com.madalla.webapp.Panels;

public class SiteAdminPage extends AdminPage implements ISecureAdminPage{

    public SiteAdminPage(final PageParameters parameters){
    	super(parameters);
        add(Panels.siteAdminPanel("adminPanel"));
    }
}
