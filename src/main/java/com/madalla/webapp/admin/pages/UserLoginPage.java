package com.madalla.webapp.admin.pages;

import org.apache.wicket.RestartResponseException;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.webapp.admin.AbstractAdminPage;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.webapp.user.UserLoginPanel;

public class UserLoginPage extends AbstractAdminPage{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(UserLoginPage.class);
	
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
			    log.trace("preLogin - username-" + username);
        		IAuthenticator authenticator = getApplicationService().getUserAuthenticator();
        		if (authenticator.requiresSecureAuthentication(username)){
        		    log.debug("preLogin - secure authentication required.");
        			redirectToSecurePage(username);
        		}

			}

		});

	}
	
	private void redirectToSecurePage(final String username) {
            log.debug("redirectToSecurePage - redirecting to secure page.");
            throw new RestartResponseException(SecureLoginPage.class, new PageParameters().set(PARAMETERS[0], username));
	}


}
