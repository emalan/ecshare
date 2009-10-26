package com.madalla.webapp.pages;

import static com.madalla.webapp.PageParams.RETURN_PAGE;

import org.apache.wicket.PageParameters;
import org.apache.wicket.protocol.https.RequireHttps;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.panel.Panels;

@RequireHttps
public class SecurePasswordPage extends AdminPage {

	public SecurePasswordPage(PageParameters params) {
		super(params);
		add(Panels.userPasswordPanel("adminPanel",params));
	}

	@Override
	protected void setupMenu(PageParameters params) {
		params.add(RETURN_PAGE, getApplication().getHomePage().getName());
		super.setupMenu(params);
	}
	
	

}
