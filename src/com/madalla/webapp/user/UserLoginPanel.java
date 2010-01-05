package com.madalla.webapp.user;

import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import com.madalla.bo.SiteData;
import com.madalla.email.IEmailSender;
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
		
		final CmsSession session = (CmsSession) getSession();
		
		final Component loginInfo = new Label("loginInfo", new StringResourceModel("login.info",new Model<CmsSession>(session))){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onBeforeRender() {
				setOutputMarkupId(true);
				setVisibilityAllowed(true);
				if (!session.isLoggedIn()){
					setVisible(false);
				}
				super.onBeforeRender();
			}

		};
		add(loginInfo);

		final Component panel = new LoginPanel("signInPanel", credentials){
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
            
			@Override
			protected void onBeforeRender() {
				setOutputMarkupId(true);
				if (session.isLoggedIn()){
					setEnabled(false);
				} else {
					setEnabled(true);
				}
				super.onBeforeRender();
			}
            
            
        };
        add(panel);
		
		add(new AjaxFallbackLink<Object>("logout"){

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				session.logout();
				target.addComponent(this);
				target.addComponent(loginInfo);
				target.addComponent(panel);
			}

			@Override
			protected void onBeforeRender() {
				setOutputMarkupId(true);
				setVisibilityAllowed(true);
				if (!session.isLoggedIn()){
					setVisible(false);
				}
				super.onBeforeRender();
			}
			
		});
		
		Component emailLink = new Label("emailLink", getString("label.support"));
		add(emailLink);
		
		MarkupContainer emailDiv = new WebMarkupContainer("emailDiv");
		add(emailDiv);
		
		SiteData site = getRepositoryService().getSiteData();
		Component emailForm = new EmailFormPanel("supportEmail", "Support email - sent from " + site.getName()){
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean sendEmail(IEmailSender emailSender, SiteData site, String body, String subject) {
				return emailSender.sendEmail(subject, body);
			}
			
		};
		emailDiv.add(emailForm);
		
		emailLink.add(new AnimationOpenSlide("onclick", emailDiv, 28,"em"));

	}
	
	protected void preLogin(String username){
		
	}

}
