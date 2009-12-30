package com.madalla.webapp.pages;

import org.apache.wicket.Page;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.webapp.user.UserLoginPanel;

public class UserLoginPage extends AdminPage{

	public UserLoginPage(){
		this("");
	}
	public UserLoginPage(String username){
		super();
		add(new UserLoginPanel("adminPanel", username){

			private static final long serialVersionUID = 1L;

			@Override
			protected void preLogin(String username) {
        		IAuthenticator authenticator = getRepositoryService().getUserAuthenticator();
        		if (authenticator.requiresSecureAuthentication(username)){
        			setResponsePage(new SecureLoginPage(username));
        		}

			}
			
		});
	}
	
	public UserLoginPage(final Class<? extends Page> returnPage, String username){
		this(username);
		setReturnPage(returnPage);
	}
}
