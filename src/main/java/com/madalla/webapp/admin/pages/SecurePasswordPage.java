package com.madalla.webapp.admin.pages;

import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.admin.AbstractAdminPage;
import com.madalla.webapp.user.UserPasswordPanel;

@RequireHttps
public class SecurePasswordPage extends AbstractAdminPage {

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
		String user = PageUtils.getPageParameter("user", parameters, "SecurePasswordPage");
		String pwd =  PageUtils.getPageParameter("pwd", parameters, "UserPasswordPanel","");
		init(user,pwd);
	}

	private void init(String username, String pwd){
		add(new UserPasswordPanel("adminPanel", new SecureCredentials().setUsername(username).setPassword(pwd)));
	}

}
