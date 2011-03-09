package com.madalla.webapp.components.member;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

import com.madalla.bo.member.MemberData;
import com.madalla.util.security.ICredentialHolder;
import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.admin.member.MemberSession;
import com.madalla.webapp.security.IPasswordAuthenticator;
import com.madalla.webapp.security.PasswordAuthenticator.UserLoginTracker;
import com.madalla.wicket.form.AjaxValidationSubmitButton;
import com.madalla.wicket.form.ValidationStyleBehaviour;

public class MemberPasswordPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(this.getClass());
	
	private final ICredentialHolder credentials = new SecureCredentials();
	
	public class MemberPasswordForm extends Form<ICredentialHolder> {
		private static final long serialVersionUID = 1L;

		public MemberPasswordForm(String id, IModel<ICredentialHolder> model, boolean validated) {
			super(id, model);
			
			PasswordTextField existingPassword = new PasswordTextField("existingPassword",new Model<String>(""));
            existingPassword.add(new ValidationStyleBehaviour());
            existingPassword.setOutputMarkupId(true);
            if (validated) {
            	existingPassword.setEnabled(false);
            }
            add(existingPassword);
            existingPassword.add(new AbstractValidator<String>(){
                private static final long serialVersionUID = 1L;

                @Override
                protected void onValidate(IValidatable<String> validatable) {
                	log.debug("Validating existing password.");
                	ICredentialHolder credentials = getModelObject();
                	if (!validateMember(new SecureCredentials().setUsername(credentials.getUsername()).setPassword(validatable.getValue()))) {
                		error(validatable,"error.existing");
                	}
                }
                
            });
            
            add(new ComponentFeedbackPanel("existingFeedback", existingPassword).setOutputMarkupId(true));
            
            PasswordTextField newPassword = new PasswordTextField("password");
            newPassword.add(new ValidationStyleBehaviour());
            newPassword.setOutputMarkupId(true);
            add(newPassword);
            add(new ComponentFeedbackPanel("newFeedback", newPassword).setOutputMarkupId(true));
            
            TextField<String> confirmPassword = new PasswordTextField("confirmPassword", new Model<String>(""));
            confirmPassword.add(new ValidationStyleBehaviour());
            confirmPassword.setOutputMarkupId(true);
            add(confirmPassword);
            add(new ComponentFeedbackPanel("confirmFeedback",confirmPassword).setOutputMarkupId(true));
            
            //Validate that new and confirm are equal
            add(new EqualPasswordInputValidator(newPassword, confirmPassword));

		}
		
	}
	
	public MemberPasswordPanel(String id, final ICredentialHolder existing) {
		super(id);
		
		this.credentials.setUsername(existing.getUsername());
		
		MemberSession session = getAppSession().getMemberSession();
		
		final boolean validated;
		final boolean loggedIn = session.isSignedIn();
		if (loggedIn) {
			validated = false; 
			log.debug("Member not validated as they are already logged in. user="+existing.getUsername());
		} else {
			validated = session.signIn(existing.getUsername(), existing.getPassword());
			log.debug("Validate --> " + validated);
		}
		
		final UserLoginTracker tracker = getLoginTracker(existing);
		
		add(new Label("processing"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
				if (validated){
					replaceComponentTagBody(markupStream, openTag, getString("info.validated"));
				} else {
					if (loggedIn) { //from inside application
						replaceComponentTagBody(markupStream, openTag, getString("info.existing", new Model<UserLoginTracker>(tracker)));
					} else { //from URL
						replaceComponentTagBody(markupStream, openTag, getString("info.validation.failed", new Model<UserLoginTracker>(tracker)));
					}
				}
			}
			
		});
		
		final Label loginCount = new Label("loginCount", new StringResourceModel("info.logincount", this, new Model<UserLoginTracker>(tracker))){
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onBeforeRender() {
				setVisibilityAllowed(true);
				if (validated){
					setVisible(false);
				}
				super.onBeforeRender();
			}
			
		};
		loginCount.setOutputMarkupId(true);
		add(loginCount);

		
		Form<ICredentialHolder> form;
		add(form = new MemberPasswordForm("passwordForm", new CompoundPropertyModel<ICredentialHolder>(credentials), validated));
		
		if (validated){
			form.add(new AttributeAppender("class", new Model<String>("validated"), " "));
		}
		
		FeedbackPanel formFeedback = new ComponentFeedbackPanel("formFeedback", form);
        form.add(formFeedback);
        
        Component passwordSubmit = new AjaxValidationSubmitButton("formSubmit", form, formFeedback){
        	private static final long serialVersionUID = 1L;
        	
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				MemberData member = getRepositoryService().getMember(credentials.getUsername());
				log.debug("process password change for : " + member);
				//sign member in - we have already validated old password
				if (getAppSession().getMemberSession().signIn(member.getMemberId(), member.getPassword())){
					member.setPassword(credentials.getPassword());
					getRepositoryService().saveMember(member);
					info(getString("message.success"));
	                setResponsePage(getCmsApplication().getMemberRegistrationPage());
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(loginCount);
				super.onError(target, form);
			}
			
        };
        
        form.add(passwordSubmit);
        
        form.add(new AttributeModifier("onSubmit", true, new Model<String>("document.getElementById('" + passwordSubmit.getMarkupId() + "').onclick();return false;")));

	}
	
    @Override
	protected void onBeforeRender() {
    	addOrReplace(new Label("heading", getString("heading.password", new Model<ICredentialHolder>(credentials))));
		super.onBeforeRender();
	}
    
    private UserLoginTracker getLoginTracker(ICredentialHolder credentials){
    	return getAuthenticator(credentials.getUsername()).getUserLoginTracker(credentials.getUsername());
    }
	
	private boolean validateMember(ICredentialHolder credentials){
		return getAuthenticator(credentials.getUsername()).authenticate(credentials.getUsername(), credentials.getPassword());
    }
	
	private IPasswordAuthenticator getAuthenticator(String name){
		return getRepositoryService().getMemberAuthenticator(name);
    }
	


}
