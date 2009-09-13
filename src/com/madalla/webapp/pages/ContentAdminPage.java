package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.service.IDataServiceProvider;
import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureAdminPage;
import com.madalla.webapp.panel.Panels;

public class ContentAdminPage extends AdminPage implements ISecureAdminPage  {
    
    public ContentAdminPage(final PageParameters parameters){
    	super(parameters);
    	
    	if (((IDataServiceProvider)getApplication()).getRepositoryService().isAdminApp()){
    		add(Panels.contentAdminPanelForAdmin("adminPanel"));
    	} else {
    		add(Panels.contentAdminPanelForSite("adminPanel"));
    	}
    }

}