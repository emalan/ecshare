package com.madalla.webapp.user;


import static com.madalla.webapp.PageParams.RETURN_PAGE;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
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
import com.madalla.util.security.ICredentialHolder;
import com.madalla.util.security.SecureCredentials;
import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.login.aware.LoggedinBookmarkablePageLink;
import com.madalla.webapp.pages.UserAdminPage;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.wicket.form.AjaxValidationStyleRequiredTextField;
import com.madalla.wicket.form.AjaxValidationStyleSubmitButton;
import com.madalla.wicket.form.ValidationStylePasswordField;

public class UserProfilePanel extends Panel{

	private static final long serialVersionUID = 9027719184960390850L;
	private static final Log log = LogFactory.getLog(UserProfilePanel.class);
	
	private UserData user;
	private ICredentialHolder credentials = new SecureCredentials();
	
    public class PasswordForm extends Form {
        
        private static final long serialVersionUID = 9033980585192727266L;
        private final ValueMap properties = new ValueMap();
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
                    String value = SecurityUtils.encrypt((String)validatable.getValue());
                    if (!user.getPassword().equals(value)){
                        error(validatable,"error.existing");
                    }
                }
                
            });
            
            
            FeedbackPanel newFeedback = new FeedbackPanel("newFeedback");
            add(newFeedback);
            TextField newPassword = new ValidationStylePasswordField("newPassword",
                    new PropertyModel(credentials,"password"), newFeedback);
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

	public UserProfilePanel(String id, String returnPage){
		
		super(id);
		
		//User admin link
		PageParameters params = new PageParameters(RETURN_PAGE + "=" + returnPage);
		add(new LoggedinBookmarkablePageLink("UserAdmin", UserAdminPage.class, params, true, true, true).setAutoEnable(true));
		
		add(JavascriptPackageResource.getHeaderContribution(Scriptaculous.PROTOTYPE));
		add(Css.CSS_FORM);

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
                user.setPassword(credentials.getPassword());
                getRepositoryService().saveUser(user);
                form.info(getString("message.success"));
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
