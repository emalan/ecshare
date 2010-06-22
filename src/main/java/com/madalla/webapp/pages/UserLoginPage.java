package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.bo.security.IUser;
import com.madalla.webapp.admin.AdminPage;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.webapp.user.UserLoginPanel;

public class UserLoginPage extends AdminPage{
	
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
		
		// TODO add panel with RPX widget
		// TODO if user has Profile then set default provider
	}
	

}
