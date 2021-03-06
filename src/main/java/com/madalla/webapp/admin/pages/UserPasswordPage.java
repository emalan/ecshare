package com.madalla.webapp.admin.pages;

import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.admin.AbstractAdminPage;
import com.madalla.webapp.user.UserPasswordPanel;

public class UserPasswordPage extends AbstractAdminPage {

	public UserPasswordPage(String username, String pwd){
		super();
		init(username, pwd);
	}
	public UserPasswordPage(PageParameters parameters) {
		super();
		String user = PageUtils.getPageParameter("user", parameters, "SecurePasswordPage");
		String pwd =  PageUtils.getPageParameter("pwd", parameters, "UserPasswordPanel","");
		init(user, pwd);
	}

	private void init(String username, String pwd){

		add(new UserPasswordPanel("adminPanel", new SecureCredentials().setUsername(username).setPassword(pwd)));
	}

}
