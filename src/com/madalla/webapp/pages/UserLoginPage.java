package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.panel.Panels;

public class UserLoginPage extends AdminPage{

	public UserLoginPage(final PageParameters parameters){
		super(parameters);
		add(Panels.userLoginPanel("adminPanel"));
	}
}
