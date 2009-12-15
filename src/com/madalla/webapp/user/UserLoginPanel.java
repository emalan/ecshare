package com.madalla.webapp.user;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;

import com.madalla.bo.SiteData;
import com.madalla.util.security.ICredentialHolder;
import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.email.EmailFormPanel;
import com.madalla.webapp.login.LoginPanel;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.webapp.scripts.JavascriptResources;
import com.madalla.wicket.animation.AnimationOpenSlide;

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

		add(JavascriptPackageResource.getHeaderContribution(JavascriptResources.ANIMATOR));

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
		
		Component emailLink = new Label("emailLink", getString("label.support"));
		add(emailLink);
		
		MarkupContainer emailDiv = new WebMarkupContainer("emailDiv");
		add(emailDiv);
		
		SiteData site = getRepositoryService().getSiteData();
		Component emailForm = new EmailFormPanel("supportEmail", "Support email - sent from " + site.getName());
		emailDiv.add(emailForm);
		
		emailLink.add(new AnimationOpenSlide("onclick", emailDiv, 27,"em"));

	}
	
	protected void preLogin(String username){
		
	}

}
