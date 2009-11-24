package com.madalla.webapp.pages;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureAdminPage;
import com.madalla.webapp.site.PageAdminPanel;

/**
 * @author Eugene Malan
 */
public class PageAdminPage extends AdminPage implements ISecureAdminPage {

	public PageAdminPage(){
		add(new PageAdminPanel("adminPanel"));
	}
}

