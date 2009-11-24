package com.madalla.webapp.pages;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureAdminPage;
import com.madalla.webapp.cms.admin.ContentAdminPanel;

public class ApplicationContentAdminPage extends AdminPage implements ISecureAdminPage{

    public ApplicationContentAdminPage(){
    	super();
        add(ContentAdminPanel.newAdminInstance("adminPanel"));
    }
}
