package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.panel.Panels;
import com.madalla.webapp.user.UserPasswordPanel;

public class UserPasswordPage extends AdminPage {

	public UserPasswordPage(String username, String pwd){
		super();
		init(username, pwd);
	}
	public UserPasswordPage(PageParameters parameters) {
		super();
		String user = Panels.getPageParameter("user", parameters, "SecurePasswordPage");
		String pwd =  Panels.getPageParameter("pwd", parameters, "UserPasswordPanel","");
		init(user, pwd);
	}
	
	private void init(String username, String pwd){
		add(new UserPasswordPanel("adminPanel",username, pwd));
	}

}
