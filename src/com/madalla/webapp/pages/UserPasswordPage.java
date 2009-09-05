package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.Panels;

public class UserPasswordPage extends AdminPage {

	public UserPasswordPage(PageParameters params) {
		super(params);
		add(Panels.userPasswordPanel("adminPanel",params));
	}

}
