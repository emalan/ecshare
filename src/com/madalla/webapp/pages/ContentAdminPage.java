package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureAdminPage;
import com.madalla.webapp.Panels;

public class ContentAdminPage extends AdminPage implements ISecureAdminPage  {
    
    public ContentAdminPage(final PageParameters parameters){
    	super(parameters);
    	
    	if (((IRepositoryServiceProvider)getApplication()).getRepositoryService().isAdminApp()){
    		add(Panels.contentAdminPanelForAdmin("adminPanel"));
    	} else {
    		add(Panels.contentAdminPanelForSite("adminPanel"));
    	}
    }

}