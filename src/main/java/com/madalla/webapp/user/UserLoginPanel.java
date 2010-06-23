package com.madalla.webapp.user;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;

import com.madalla.bo.SiteData;
import com.madalla.bo.security.IUser;
import com.madalla.email.IEmailSender;
import com.madalla.util.security.ICredentialHolder;
import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.CmsApplication;
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
		
		final Component loginInfo = new Label("loginInfo", new StringResourceModel("login.info",new Model<IUser>(getSessionDataService().getUser()))){
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
				// TODO hide password login if user has no password
				
				IUser user = getRepositoryService().getUser(username);
				getAppSession().setUser(user);
				
        		if (getApplication().getConfigurationType().equals(Application.DEVELOPMENT)){
        			return;
        		}
        		preLogin(username);
			}
           	
            @Override
            public boolean signIn(String username, String password) {
            	CmsSession session = (CmsSession) getSession();
            	return session.signIn(username, password);
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
				session.signOut();
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
		
		add(new WebComponent("openidWidget"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				final String siteUrl = getRepositoryService().getSiteData().getUrl();
				final String lang = getSession().getLocale().getLanguage();
				final String hideHeading = "flags=hide_sign_in_with";
				if (StringUtils.isNotEmpty(siteUrl)){
					String callback = ((CmsApplication)getApplication()).getRpxService().getCallback();
					tag.put("src", MessageFormat.format(callback, new Object[]{siteUrl, lang}) + "&" + hideHeading);
				}
			}
			
			
		});

	}
	
	protected void preLogin(String username){
		
	}

}
