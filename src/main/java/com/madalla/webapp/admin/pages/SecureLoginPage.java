package com.madalla.webapp.admin.pages;

import org.apache.wicket.protocol.https.RequireHttps;

import com.madalla.webapp.admin.AbstractAdminPage;
import com.madalla.webapp.user.UserLoginPanel;

@RequireHttps
public class SecureLoginPage extends AbstractAdminPage{

	private static final long serialVersionUID = 1L;

    public SecureLoginPage(){
		this("");
	}

	public SecureLoginPage(String username) {
		add(new UserLoginPanel("adminPanel", username));
	}

}
