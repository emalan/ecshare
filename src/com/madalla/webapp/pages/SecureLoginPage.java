package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.protocol.https.RequireHttps;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.panel.Panels;

@RequireHttps
public class SecureLoginPage extends AdminPage{
	
	public SecureLoginPage(PageParameters params) {
		this(params,"");
	}

	public SecureLoginPage(PageParameters params, String username) {
		super(params);
		add(Panels.userLoginPanel("adminPanel", username));
	}

}
