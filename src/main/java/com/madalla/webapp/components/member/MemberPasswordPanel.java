package com.madalla.webapp.components.member;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;

import com.madalla.util.security.ICredentialHolder;
import com.madalla.util.security.SecureCredentials;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.security.IPasswordAuthenticator;
import com.madalla.wicket.form.ValidationStyleBehaviour;

public class MemberPasswordPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(this.getClass());
	
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
		
		final ICredentialHolder credentials = new SecureCredentials();
		credentials.setUsername(existing.getUsername());
		
		final boolean validated;
		final boolean loggedIn = ((CmsSession)getSession()).isLoggedIn();
		if (loggedIn) {
			validated = false; 
		} else {
			validated = validateMember(existing);
		}
		
		add(new MemberPasswordForm("passwordForm", new CompoundPropertyModel<ICredentialHolder>(credentials), validated));
	}
	
	private boolean validateMember(ICredentialHolder credentials){
		return getAuthenticator(credentials.getUsername()).authenticate(credentials.getUsername(), credentials.getPassword());
    }
	
	private IPasswordAuthenticator getAuthenticator(String name){
		return getRepositoryService().getMemberAuthenticator(name);
    }
	


}
