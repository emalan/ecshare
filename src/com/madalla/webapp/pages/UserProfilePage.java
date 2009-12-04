package com.madalla.webapp.pages;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureWebPage;
import com.madalla.webapp.user.UserProfilePanel;

public class UserProfilePage extends AdminPage implements ISecureWebPage  {

	public UserProfilePage(){
		add(new UserProfilePanel("adminPanel"));
	}
}
