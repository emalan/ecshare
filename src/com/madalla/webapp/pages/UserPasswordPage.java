package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.AdminPage;
import com.madalla.webapp.user.UserPasswordPanel;

public class UserPasswordPage extends AdminPage {

	public UserPasswordPage(String username, String pwd){
		super();
		init(username, pwd);
	}
	public UserPasswordPage(PageParameters parameters) {
		super();
		String user = PageUtils.getPageParameter("user", parameters, "SecurePasswordPage");
		String pwd =  PageUtils.getPageParameter("pwd", parameters, "UserPasswordPanel","");
		init(user, pwd);
		setReturnPage(getApplication().getHomePage());
	}
	
	private void init(String username, String pwd){

		add(new UserPasswordPanel("adminPanel", new SecureCredentials().setUsername(username).setPassword(pwd)));
	}

}
