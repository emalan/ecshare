package com.madalla.webapp.pages;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureSuperPage;
import com.madalla.webapp.user.UserAdminPanel;

public class UserAdminPage extends AdminPage implements ISecureSuperPage {

	public UserAdminPage(){
		super();
		add(new UserAdminPanel("adminPanel"));
	}
}
