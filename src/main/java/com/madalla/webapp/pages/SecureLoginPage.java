package com.madalla.webapp.pages;

import org.apache.wicket.protocol.https.RequireHttps;

import com.madalla.webapp.admin.AdminPage;
import com.madalla.webapp.user.UserLoginPanel;

@RequireHttps
public class SecureLoginPage extends AdminPage{
	
	public SecureLoginPage(){
		this("");
	}

	public SecureLoginPage(String username) {
		add(new UserLoginPanel("adminPanel", username));
	}

}