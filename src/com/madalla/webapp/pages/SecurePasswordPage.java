package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.protocol.https.RequireHttps;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.panel.Panels;
import com.madalla.webapp.user.UserPasswordPanel;

@RequireHttps
public class SecurePasswordPage extends AdminPage {

	public SecurePasswordPage(String username, String pwd){
		super();
		init(username, pwd);
	}
	
	/**
	 * Page needs to be accessed from outside app for password changes
	 * 
	 * @param parameters
	 */
	public SecurePasswordPage(PageParameters parameters) {
		super();
		String user = Panels.getPageParameter("user", parameters, "SecurePasswordPage");
		String pwd =  Panels.getPageParameter("pwd", parameters, "UserPasswordPanel","");
		init(user,pwd);
	}
	
	private void init(String username, String pwd){
		add(new UserPasswordPanel("adminPanel",username, pwd));
	}

}
