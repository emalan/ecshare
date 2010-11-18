package com.madalla.webapp.user;

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
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

import com.madalla.bo.security.UserData;
import com.madalla.util.security.ICredentialHolder;
import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.security.IPasswordAuthenticator;
import com.madalla.webapp.security.PasswordAuthenticator.UserLoginTracker;
import com.madalla.wicket.form.AjaxValidationSubmitButton;
import com.madalla.wicket.form.ValidationStyleBehaviour;

public class UserPasswordPanel extends CmsPanel{

	private static final long serialVersionUID = 3207705274626512033L;
	private static final Log log = LogFactory.getLog(UserPasswordPanel.class);

	private final ICredentialHolder credentials = new SecureCredentials();
	
    public class PasswordForm extends Form<UserData> {
        
        private static final long serialVersionUID = 9033980585192727266L;
        
        public PasswordForm(String id, boolean validated) {
            super(id);
            
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
                	if (!validateUser(new SecureCredentials().setUsername(credentials.getUsername()).setPassword(validatable.getValue()))) {
                		error(validatable,"error.existing");
                	}
                }
                
            });

            add(new ComponentFeedbackPanel("existingFeedback", existingPassword).setOutputMarkupId(true));
            
            PasswordTextField newPassword = new PasswordTextField("newPassword",
                    new PropertyModel<String>(credentials,"password"));
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
    
    public UserPasswordPanel(final String id, final String username){
    	this(id, new SecureCredentials().setUsername(username));
    }
    
    public UserPasswordPanel(final String id, final ICredentialHolder existing){
    	super(id);

    	this.credentials.setUsername(existing.getUsername());

		add(Css.CSS_FORM);
		
		final boolean validated;
		final boolean loggedIn = ((CmsSession)getSession()).isLoggedIn();
		if (loggedIn) {
			validated = false; 
		} else {
			validated = validateUser(existing);
		}
		
		final UserLoginTracker tracker = getUserLoginInfo(existing);
		
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


        Form<UserData> form = new PasswordForm("passwordForm", validated);
        add(form);
        
        //if validated set class of form to validated
        if (validated) {
        	form.add(new AttributeAppender("class", new Model<String>("validated"), " "));
        }
        
        FeedbackPanel formFeedback = new ComponentFeedbackPanel("formFeedback", form);
        form.add(formFeedback);
        
        Component passwordSubmit = new AjaxValidationSubmitButton("formSubmit", form, formFeedback){
        	private static final long serialVersionUID = 1L;
        	
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				UserData user = getRepositoryService().getUser(credentials.getUsername());
				if (((CmsSession)getSession()).signIn(user.getName(), user.getPassword())){
	                user.setPassword(credentials.getPassword());
	                saveData(user);
	                info(getString("message.success"));
	                setResponsePage(getApplication().getHomePage());
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
    
    private UserLoginTracker getUserLoginInfo(ICredentialHolder credentials){
    	return getAuthenticator(credentials.getUsername()).getUserLoginTracker(credentials.getUsername());
    }

	private boolean validateUser(ICredentialHolder credentials){
		return getAuthenticator(credentials.getUsername()).authenticate(credentials.getUsername(), credentials.getPassword());
    }
	
	private IPasswordAuthenticator getAuthenticator(String username){
		return getRepositoryService().getPasswordAuthenticator(credentials.getUsername());
	}

}
