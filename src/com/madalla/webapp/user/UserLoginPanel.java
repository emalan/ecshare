package com.madalla.webapp.user;

import org.apache.wicket.Page;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.webapp.signIn.SignInPanel;

public class UserLoginPanel extends Panel {

	private static final long serialVersionUID = 5349334518027160490L;

	public UserLoginPanel(String id, Class<? extends Page> returnPage) {
		super(id);
		
		add(HeaderContributor.forJavaScript(Scriptaculous.PROTOTYPE));
		
		add(new PageLink("returnLink", returnPage));
		
		add(new SignInPanel("signInPanel", new SecureCredentials()){
            private static final long serialVersionUID = 1L;
            
            public boolean signIn(String username, String password) {
            	CmsSession session = (CmsSession) getSession();
                if (session.login(username, password)) {
                    return true;
                } else {
                    return false;
                }

            }
            
            protected void onSignInSucceeded() {
                super.onSignInSucceeded();
            }
            
        });

	}

}
