package com.madalla.webapp.user;

import org.apache.wicket.Application;

import com.madalla.bo.SiteData;
import com.madalla.util.security.ICredentialHolder;
import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.email.EmailFormPanel;
import com.madalla.webapp.login.LoginPanel;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.webapp.scripts.JavascriptResources;

public class UserLoginPanel extends CmsPanel {

	private static final long serialVersionUID = 5349334518027160490L;
	
	public UserLoginPanel(String id) {
		this(id, new SecureCredentials());
	}

	public UserLoginPanel(String id, String username) {
		this(id, new SecureCredentials().setUsername(username));
	}
	
	private UserLoginPanel(String id, ICredentialHolder credentials){
		super(id);

		add(JavascriptResources.ANIMATOR);

		add(new LoginPanel("signInPanel", credentials){
            private static final long serialVersionUID = 1L;
            
           	@Override
			protected void preSignIn(String username) {
        		if (getApplication().getConfigurationType().equals(Application.DEVELOPMENT)){
        			return;
        		}
        		preLogin(username);
			}
           	
            @Override
            public boolean signIn(String username, String password) {
            	CmsSession session = (CmsSession) getSession();
                if (session.login(username, password)) {
                    return true;
                } else {
                    return false;
                }

            }
            
            
        });
		
		SiteData site = getRepositoryService().getSiteData();
		add(new EmailFormPanel("supportEmail", "Support email - sent from " + site.getName()));

	}
	
	protected void preLogin(String username){
		
	}

}
