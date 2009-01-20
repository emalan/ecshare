package com.madalla.webapp.user;

import org.apache.wicket.Page;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.webapp.CmsSession;
import com.madalla.webapp.signIn.SignInPanel;

public class UserLoginPanel extends Panel {

	private static final long serialVersionUID = 5349334518027160490L;

	public UserLoginPanel(String id, Class<? extends Page> returnPage) {
		super(id);
		
		add(new PageLink("returnLink", returnPage));
		
        // Create feedback panel and add to page
        final FeedbackPanel feedback = new FeedbackPanel("loginFeedback");
        add(feedback);
        
		add(new SignInPanel("signInPanel", feedback){
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
