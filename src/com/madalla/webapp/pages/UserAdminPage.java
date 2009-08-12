package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureSuperPage;
import com.madalla.webapp.Panels;

public class UserAdminPage extends AdminPage implements ISecureSuperPage {

	public UserAdminPage(final PageParameters parameters){
		super(parameters);
		add(Panels.userAdminPanel("adminPanel"));
	}
}
