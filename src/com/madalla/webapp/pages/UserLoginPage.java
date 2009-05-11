package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.Panels;

public class UserLoginPage extends AdminPage{

	public UserLoginPage(final PageParameters params){
		
		add(Panels.userLoginPanel("adminPanel", params));
	}
}
