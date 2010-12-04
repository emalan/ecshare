package com.madalla.webapp.components.member;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.model.util.MapModel;

import com.madalla.bo.SiteData;
import com.madalla.bo.member.MemberData;
import com.madalla.util.security.ICredentialHolder;
import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.CmsApplication;
import com.madalla.webapp.admin.member.MemberSession;

public class MemberLoginPanel extends AbstractMemberPanel{
	private static final long serialVersionUID = 1L;
	private final static Log log = LogFactory.getLog(MemberLoginPanel.class);
	
	public abstract class SignInform extends Form<Object>{
		private static final long serialVersionUID = 1L;
		
		public SignInform(final String id, final ICredentialHolder credentials) {
			super(id);
			
			final TextField<String> username;
			add(username = new RequiredTextField<String>("username", new PropertyModel<String>(credentials, "username")));
			username.setRequired(true);
			
			final PasswordTextField password;
			add(password = new PasswordTextField("password", new PropertyModel<String>(credentials,"password")));
            password.setRequired(false);
            
//			add(new CheckBox("rememberMe", new PropertyModel<Boolean>(LoginPanel.this,
//					"rememberMe")));
			
			username.setPersistent(true);
		}
		
		public abstract boolean signIn(String username, String password);
		
	}
	
	public MemberLoginPanel(String id){
		this(id, new SecureCredentials(), null);
	}

	public MemberLoginPanel(String id, final ICredentialHolder credentials, final Class<? extends Page> destination) {
		super(id);
		
		final MemberSession session = getAppSession().getMemberSession();
		
		final Component loginInfo = new Label("loginInfo", new StringResourceModel("login.info",new Model<MemberData>(session.getMember()))){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onBeforeRender() {
				setOutputMarkupId(true);
				setVisibilityAllowed(true);
				if (!session.isSignedIn()){
					setVisible(false);
				}
				super.onBeforeRender();
			}

		};
		add(loginInfo);
		
		final Form<Object> signinForm = new SignInform("signInForm", credentials){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean signIn(String username, String password) {
				return session.signIn(username, password);
			}
			
		};
		add(signinForm);
		
		final FeedbackPanel feedback = new FeedbackPanel("loginFeedback");
		feedback.setOutputMarkupId(true);
		signinForm.add(feedback);
		
		signinForm.add(new IndicatingAjaxButton("submitLink", signinForm){

            private static final long serialVersionUID = 1L;

            @Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.debug("Ajax onError called");
				target.addComponent(feedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				log.debug("Ajax submit called");
				target.addComponent(feedback);
				
//					if (signIn(getUsername(), getPassword())){
//						feedback.info(getLocalizer().getString("signInFailed", this, "Success"));
//						onSignInSucceeded(target);
//					} else {
//						feedback.error(getLocalizer().getString("signInFailed", this, "Sign in failed"));
//						target.addComponent(feedback);
//						onSignInFailed(getUsername());
//					}
				
				
			}
			
		});
		
		add(new AjaxFallbackLink<Object>("logout"){

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				session.signOut();
				target.addComponent(this);
				target.addComponent(loginInfo);
				processSignOut();
			}

			@Override
			protected void onBeforeRender() {
				setOutputMarkupId(true);
				setVisibilityAllowed(true);
				if (!session.isSignedIn()){
					setVisible(false);
				}
				super.onBeforeRender();
			}
			
		});
		
		
		final Form<String> resetForm;
		add(resetForm = new Form<String>("resetForm"));
		
		final TextField<String> username ;
		resetForm.add(username = new RequiredTextField<String>("memberId", new Model<String>(credentials.getUsername())));

		final Component resetFeedback;
		resetForm.add(resetFeedback = new ComponentFeedbackPanel("resetFeedback", username).setOutputMarkupId(true));
		resetForm.add(new IndicatingAjaxButton("submitLink", resetForm) {

			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(resetFeedback);
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				final String memberId = username.getModelObject();
				
				if (memberExists(memberId)) {
					final MemberData member = getRepositoryService().getMember(memberId);
					final String email = member.getEmail();
					if (StringUtils.isNotEmpty(email)){
						if (sendEmail(member)){
							username.info(getString("message.success"));
						} else {
							log.error("password reset - Send failure! " + member);
							username.error(getString("error.reset"));
						}
					} else {
						log.info("password reset - Unable to send email. No value for email. " + member);
						username.error(getString("error.reset"));
					}
				} else {
					username.error(getString("error.reset"));
				}
				target.addComponent(feedback);
			}	
		});
	}
	
	private boolean memberExists(String memberId){
		return getRepositoryService().isMemberExist(memberId);
	}
		
	protected void processSignOut(){
		
	}
	
	@Override
    protected String getEmailBody(final MemberData member){
    	Map<String, String> map = new HashMap<String, String>();
    	SiteData site = getRepositoryService().getSiteData();
    	map.put("siteName", site.getSiteName());
    	map.put("firstName", StringUtils.defaultString(member.getFirstName()));
		map.put("lastName", StringUtils.defaultString(member.getLastName()));
		map.put("companyName", StringUtils.defaultString(member.getCompanyName()));
		map.put("memberId", member.getMemberId());
		map.put("password", resetPassword(member));
		String url = StringUtils.defaultString(site.getUrl());
		map.put("url", url );
    	map.put("passwordChangePage", CmsApplication.MEMBER_PASSWORD);
		
		MapModel<String, String> values = new MapModel<String, String>(map);
		String message = getString("email.registration", values);
		
		message = message + getString("message.password", values);

		message = message + getString("message.note") + getString("message.closing");
		
		log.debug("formatMessage - " + message);
    	return message;
    }
    
    @Override
    protected String getEmailSubject(){
    	return "Registration";
    }
	


}
