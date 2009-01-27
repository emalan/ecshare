package com.madalla.webapp.user;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.madalla.bo.security.UserData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.wicket.form.AjaxValidationStyleRequiredTextField;
import com.madalla.wicket.form.AjaxValidationStyleSubmitButton;
import com.madalla.wicket.form.ValidationStylePasswordField;

public class UserProfilePanel extends Panel{

	private static final long serialVersionUID = 9027719184960390850L;
	private static final Log log = LogFactory.getLog(UserProfilePanel.class);
	
	private UserData user;
	//TODO replace ValueMap with Secure Class to hold passwords
	private final ValueMap properties = new ValueMap();
	
    public class PasswordForm extends Form {
        
        private static final long serialVersionUID = 9033980585192727266L;
        
        public PasswordForm(String id) {
            super(id);
            
            FeedbackPanel existingFeedback = new FeedbackPanel("existingFeedback");
            add(existingFeedback);
            PasswordTextField existingPassword = new ValidationStylePasswordField("existingPassword", 
            		new PropertyModel(properties,"existingPassword"), existingFeedback);
            add(existingPassword);
            existingPassword.add(new AbstractValidator(){
                private static final long serialVersionUID = 1L;

                @Override
                protected void onValidate(IValidatable validatable) {
                    log.debug("Existing Password Validation. "+validatable.getValue());
                    String value = SecurityUtils.encrypt((String)validatable.getValue());
                    if (!user.getPassword().equals(value)){
                        error(validatable,"error.existing");
                    }
                }
                
            });
            
            
            FeedbackPanel newFeedback = new FeedbackPanel("newFeedback");
            add(newFeedback);
            TextField newPassword = new ValidationStylePasswordField("newPassword",
                    new PropertyModel(properties,"newPassword"), newFeedback);
            add(newPassword);
            
            FeedbackPanel confirmFeedback = new FeedbackPanel("confirmFeedback");
            add(confirmFeedback);
            TextField confirmPassword = new ValidationStylePasswordField("confirmPassword", 
            		new PropertyModel(properties,"confirmPassword"), confirmFeedback);
            add(confirmPassword);
            
            //Validate that new and confirm are equal
            add(new EqualPasswordInputValidator(newPassword, confirmPassword));
            
        }
    }

	
    public class ProfileForm extends Form {
        private static final long serialVersionUID = -2684823497770522924L;
        
        public ProfileForm(String id) {
            super(id);
            
            FeedbackPanel emailFeedback = new FeedbackPanel("emailFeedback");
            add(emailFeedback);
            TextField email = new AjaxValidationStyleRequiredTextField("email",new PropertyModel(user,"email"), emailFeedback);
            email.add(EmailAddressValidator.getInstance());
            add(email);
            
            add(new TextField("firstName", new PropertyModel(user,"firstName")));
            add(new TextField("lastName", new PropertyModel(user,"lastName")));
        }
    }

	public UserProfilePanel(String id, Class<? extends Page> returnPage){
		
		super(id);
		
		add(HeaderContributor.forJavaScript(Scriptaculous.PROTOTYPE));
		
		add(new PageLink("returnLink", returnPage));

		//get logged in User Data
		String username = ((CmsSession)getSession()).getUsername();
		log.debug("Retrieved User name from Session. username="+username);
        user = getRepositoryService().getUser(username);
        log.debug(user);

        //****************
        //Password Section
        
        //Form
        Form passwordForm = new PasswordForm("passwordForm");
        passwordForm.setOutputMarkupId(true);
        
        add(passwordForm);
        
        final FeedbackPanel passwordFeedback = new ComponentFeedbackPanel("passwordFeedback",passwordForm);
        passwordFeedback.setOutputMarkupId(true);
        passwordForm.add(passwordFeedback);
        
        AjaxButton passwordSubmit = new AjaxValidationStyleSubmitButton("passwordSubmit", passwordForm){
        	private static final long serialVersionUID = 1L;
        	
            @Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				target.addComponent(passwordFeedback);
                IAuthenticator authenticator = getRepositoryService().getUserAuthenticator();
                if(authenticator.authenticate(user.getName(), SecurityUtils.encrypt(properties.getString("existingPassword")))){
                    user.setPassword(SecurityUtils.encrypt(properties.getString("newPassword")));
                    getRepositoryService().saveUser(user);
                    form.info(getString("message.success"));
                } else {
                    form.error(getString("message.fail"));
                }
			}

            @Override
            protected void onError(final AjaxRequestTarget target, Form form) {
            	super.onError(target, form);
            	target.addComponent(passwordFeedback);
            }
        };
        passwordForm.add(passwordSubmit);
        passwordForm.add(new AttributeModifier("onSubmit", true, new Model("document.getElementById('" + passwordSubmit.getMarkupId() + "').onclick();return false;")));
        
        
        //****************
        //Profile Section
        
		//Heading
		add(new Label("profileHeading",getString("heading.profile", new Model(user) )));
		
		//Form
		Form profileForm = new ProfileForm("profileForm");
		profileForm.setOutputMarkupId(true);
		add(profileForm);
		
		final FeedbackPanel profileFeedback = new ComponentFeedbackPanel("profileFeedback",profileForm);
		profileFeedback.setOutputMarkupId(true);
		profileForm.add(profileFeedback);
		
        AjaxButton submitButton = new AjaxValidationStyleSubmitButton("profileSubmit", profileForm){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				target.addComponent(profileFeedback);

				getRepositoryService().saveUser(user);
				form.info(getString("message.success"));
				//setResponsePage(this.findPage());
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form) {
				super.onError(target, form);
				target.addComponent(profileFeedback);

			}
        };
        profileForm.add(submitButton);
		
		
	}
	
    private IRepositoryService getRepositoryService(){
    	return ((IRepositoryServiceProvider)getApplication()).getRepositoryService();
    }
}
