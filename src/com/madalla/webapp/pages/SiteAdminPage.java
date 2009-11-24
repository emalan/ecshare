package com.madalla.webapp.pages;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureAdminPage;
import com.madalla.webapp.site.SiteAdminPanel;

public class SiteAdminPage extends AdminPage implements ISecureAdminPage{

    public SiteAdminPage(){
   		add(new SiteAdminPanel("adminPanel"));
    }
}
