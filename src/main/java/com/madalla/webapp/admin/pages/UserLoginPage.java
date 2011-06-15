package com.madalla.webapp.admin.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.admin.AbstractAdminPage;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.webapp.user.UserLoginPanel;

public class UserLoginPage extends AbstractAdminPage{

	public static final String[] PARAMETERS = {"username"};

	public UserLoginPage(PageParameters parameters){
		super(parameters);
		String username = PageUtils.getPageParameter(PARAMETERS[0], parameters, "UserLoginPage", "");

		init(username);
	}
	private void init(String username){
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


}
