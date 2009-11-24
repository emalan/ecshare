package com.madalla.webapp.pages;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureAdminPage;
import com.madalla.webapp.site.SiteDataPanel;

public class SiteDataPage extends AdminPage implements ISecureAdminPage{

	public SiteDataPage() {
		add(new SiteDataPanel("adminPanel"));
	}

}
