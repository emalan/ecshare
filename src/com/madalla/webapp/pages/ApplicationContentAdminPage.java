package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureAdminPage;
import com.madalla.webapp.panel.Panels;

public class ApplicationContentAdminPage extends AdminPage implements ISecureAdminPage{

    public ApplicationContentAdminPage(final PageParameters parameters){
    	super(parameters);
        add(Panels.contentAdminPanelForAdmin("adminPanel"));
    }
}
