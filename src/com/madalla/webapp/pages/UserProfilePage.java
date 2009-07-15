package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureWebPage;
import com.madalla.webapp.Panels;

public class UserProfilePage extends AdminPage implements ISecureWebPage  {

	public UserProfilePage(final PageParameters params){
		super(params);
		add(Panels.userProfilePanel("adminPanel"));
	}
}
