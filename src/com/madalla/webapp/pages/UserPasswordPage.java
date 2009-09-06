package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.protocol.https.RequireHttps;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.Panels;

@RequireHttps
public class UserPasswordPage extends AdminPage {

	public UserPasswordPage(PageParameters params) {
		super(params);
		add(Panels.userPasswordPanel("adminPanel",params));
	}

}
