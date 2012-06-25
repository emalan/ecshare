package com.madalla.webapp.admin.pages;

import org.apache.wicket.protocol.https.RequireHttps;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.webapp.admin.AbstractAdminPage;
import com.madalla.webapp.user.UserLoginPanel;

@RequireHttps
public class SecureLoginPage extends AbstractAdminPage{

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(SecureLoginPage.class);
	
    public SecureLoginPage(){
		init("");
	}
    
    public SecureLoginPage(final PageParameters parameters) {
        super(parameters);
        String username = PageUtils.getPageParameter(UserLoginPage.PARAMETERS[0], parameters, "UserLoginPage", "");
        init(username);
    }

	private void init(final String username) {
	    log.debug("init - username=" + username);
	    add(new UserLoginPanel("adminPanel", username));
	}

}
