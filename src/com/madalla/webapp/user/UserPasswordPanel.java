package com.madalla.webapp.user;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

import com.madalla.bo.security.UserData;
import com.madalla.util.security.ICredentialHolder;
import com.madalla.util.security.SecureCredentials;
import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.wicket.form.AjaxValidationSubmitButton;
import com.madalla.wicket.form.ValidationStyleBehaviour;

public class UserPasswordPanel extends CmsPanel{

	private static final long serialVersionUID = 3207705274626512033L;

	private ICredentialHolder credentials = new SecureCredentials();
	//private UserData user;
	
    public class PasswordForm extends Form<UserData> {
        
        private static final long serialVersionUID = 9033980585192727266L;
        private final ValueMap properties = new ValueMap();
        
        public PasswordForm(String id, IModel<UserData> model, String pwd) {
            super(id, model);
            
            properties.add("existingPassword", pwd);
            
            PasswordTextField existingPassword = new PasswordTextField("existingPassword", 
            		new PropertyModel<String>(properties,"existingPassword"));
            existingPassword.add(new ValidationStyleBehaviour());
            existingPassword.setOutputMarkupId(true);
            if (StringUtils.isNotEmpty(pwd)) {
            	existingPassword.setEnabled(false);
            	existingPassword.info("Prepopulated with existing password.");
            }
            add(existingPassword);
            existingPassword.add(new AbstractValidator<String>(){
                private static final long serialVersionUID = 1L;

                @Override
                protected void onValidate(IValidatable<String> validatable) {
                    String value = SecurityUtils.encrypt((String)validatable.getValue());
                    if (!getModelObject().getPassword().equals(value)){
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
            
            TextField<String> confirmPassword = new PasswordTextField("confirmPassword", 
            		new PropertyModel<String>(properties,"confirmPassword"));
            confirmPassword.add(new ValidationStyleBehaviour());
            confirmPassword.setOutputMarkupId(true);
            add(confirmPassword);
            add(new ComponentFeedbackPanel("confirmFeedback",confirmPassword).setOutputMarkupId(true));
            
            //Validate that new and confirm are equal
            add(new EqualPasswordInputValidator(newPassword, confirmPassword));
            
        }

    }
    public UserPasswordPanel(String id, String userName){
    	this(id,userName,"");
    }
    
    public UserPasswordPanel(String id, String userName, String existingPassword){
    	super(id);
    	
		add(Css.CSS_FORM);

		UserData user = getRepositoryService().getUser(userName);
        
    	//****************
        //Password Section
        
        //Form
        Form<UserData> form = new PasswordForm("passwordForm", new Model<UserData>(user), existingPassword);
        add(form);
        
        FeedbackPanel formFeedback = new ComponentFeedbackPanel("formFeedback", form);
        form.add(formFeedback);
        
        Component passwordSubmit = new AjaxValidationSubmitButton("formSubmit", form, formFeedback){
        	private static final long serialVersionUID = 1L;
        	
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				UserData user = (UserData) form.getModelObject();
				if (((CmsSession)getSession()).login(user.getName(), user.getPassword())){
	                user.setPassword(credentials.getPassword());
	                saveData(user);
	                info(getString("message.success"));
	                setResponsePage(getApplication().getHomePage());
				}			
			}
        	
        };
        
        form.add(passwordSubmit);
        
        form.add(new AttributeModifier("onSubmit", true, new Model<String>("document.getElementById('" + passwordSubmit.getMarkupId() + "').onclick();return false;")));
        
    }

}
