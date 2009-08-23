package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureAdminPage;
import com.madalla.webapp.Panels;

public class SiteDataPage extends AdminPage implements ISecureAdminPage{

	public SiteDataPage(PageParameters params) {
		super(params);
		add(Panels.siteDataPanel("adminPanel"));
	}

}
