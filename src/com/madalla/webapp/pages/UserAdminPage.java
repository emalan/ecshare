package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureAdminPage;
import com.madalla.webapp.Panels;

public class UserAdminPage extends AdminPage implements ISecureAdminPage {

	public UserAdminPage(final PageParameters parameters){
		add(Panels.userAdminPanel("userPanel", parameters));
	}
}
