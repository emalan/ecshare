package com.madalla.webapp.pages;

import com.madalla.service.IDataServiceProvider;
import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureAdminPage;
import com.madalla.webapp.cms.admin.ContentAdminPanel;

public class ContentAdminPage extends AdminPage implements ISecureAdminPage  {
    
    public ContentAdminPage(){
    	
    	if (((IDataServiceProvider)getApplication()).getRepositoryService().isAdminApp()){
    		add(ContentAdminPanel.newAdminInstance("adminPanel"));
    	} else {
    		add(ContentAdminPanel.newInstance("adminPanel"));
    	}
    }

}