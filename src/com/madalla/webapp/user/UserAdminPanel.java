package com.madalla.webapp.user;


import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.StringAutoCompleteRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.madalla.bo.security.UserData;
import com.madalla.email.IEmailSender;
import com.madalla.email.IEmailServiceProvider;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.wicket.form.AjaxValidationStyleRequiredTextField;
import com.madalla.wicket.form.AjaxValidationStyleSubmitButton;
import com.madalla.wicket.form.AjaxValidationStyleSubmitLink;

public class UserAdminPanel extends Panel{

	private static final long serialVersionUID = 9027719184960390850L;
	private static final Log log = LogFactory.getLog(UserAdminPanel.class);
	
	private UserData user = new UserDataView();
	private boolean lockUsername = false;
	private TextField username;
	
    public class NewUserForm extends Form {
        private static final long serialVersionUID = 9033980585192727266L;

        public NewUserForm(String id) {
            super(id);
            username = new AjaxValidationStyleRequiredTextField("username", 
            		new PropertyModel(user, "name")){

				private static final long serialVersionUID = 1L;

				@Override
				protected void onBeforeRender() {
					if(lockUsername){
						setEnabled(false);
					} else {
						setEnabled(true);
					}
					super.onBeforeRender();
				}
            	
            };
            username.setOutputMarkupId(true);
            username.add(new AutoCompleteBehavior(new StringAutoCompleteRenderer()){

				private static final long serialVersionUID = 1L;

				@Override
				protected Iterator getChoices(String input) {
					 if (Strings.isEmpty(input))
		                {
		                    return Collections.EMPTY_LIST.iterator();
		                }

		                List choices = new ArrayList(10);

		                Locale[] locales = Locale.getAvailableLocales();

		                for (int i = 0; i < locales.length; i++)
		                {
		                    final Locale locale = locales[i];
		                    final String country = locale.getDisplayCountry();

		                    if (country.toUpperCase().startsWith(input.toUpperCase()))
		                    {
		                        choices.add(country);
		                        if (choices.size() == 10)
		                        {
		                            break;
		                        }
		                    }
		                }

		                return choices.iterator();
				}
            	
            });
            add(username);
        }
    }
	
    public class ProfileForm extends Form {
        private static final long serialVersionUID = -2684823497770522924L;
        
        public ProfileForm(String id) {
            super(id);
            
            FeedbackPanel emailFeedback = new FeedbackPanel("emailFeedback");
            add(emailFeedback);
            TextField email = new AjaxValidationStyleRequiredTextField("email",
            		new PropertyModel(user,"email"), emailFeedback);
            email.add(EmailAddressValidator.getInstance());
            add(email);
            
            add(new TextField("firstName", new PropertyModel(user,"firstName")));
            add(new TextField("lastName", new PropertyModel(user,"lastName")));
        }
    }

	public UserAdminPanel(String id, Class<? extends Page> returnPage){
		
		super(id);
		
		add(HeaderContributor.forJavaScript(Scriptaculous.PROTOTYPE));
		add(Css.CSS_FORM);
		
		add(new PageLink("returnLink", returnPage));

        final Form userForm = new NewUserForm("userForm");
        userForm.setOutputMarkupId(true);
        add(userForm);
        
        final FeedbackPanel userFeedback = new ComponentFeedbackPanel("userFeedback",userForm);
        userFeedback.setOutputMarkupId(true);
        userForm.add(userFeedback);
        
        //User edit form
		final Form profileForm = new ProfileForm("profileForm");
		profileForm.setOutputMarkupId(true);
		profileForm.add(new SimpleAttributeModifier("class","formHide"));
		add(profileForm);
        
        AjaxSubmitLink newUserSubmit = new AjaxValidationStyleSubmitLink("userSubmit", userForm){
        	private static final long serialVersionUID = 1L;
        	
        	
            @Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				target.addComponent(userFeedback);
                
				String password = SecurityUtils.getGeneratedPassword();
				UserData newUser = getRepositoryService().getNewUser(user.getName(),
                		SecurityUtils.encrypt(password));
                if (newUser != null){
                	user = newUser;
                	log.debug("New User created." + user);
                	profileForm.add(new SimpleAttributeModifier("class","formShow"));
                	target.addComponent(profileForm);
                	lockUsername = true;
                	target.addComponent(userForm);
                } else {
                	log.error("Unable to create new User");
                	error("Failed to Create New User.");
                }
			}

            @Override
            protected void onError(final AjaxRequestTarget target, Form form) {
            	super.onError(target, form);
            	target.addComponent(userFeedback);
            }
            @Override
			protected final void onBeforeRender(){
            	if(lockUsername){
            		setEnabled(false);
            	} else {
            		setEnabled(true);
            	}
            	super.onBeforeRender();
            }
        };
        newUserSubmit.setOutputMarkupId(true);
        userForm.add(newUserSubmit);
        userForm.add(new AttributeModifier("onSubmit", true, new Model("document.getElementById('" + newUserSubmit.getMarkupId() + "').onclick();return false;")));
        
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
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form) {
				super.onError(target, form);
				target.addComponent(profileFeedback);

			}
        };
        profileForm.add(submitButton);
        
        AjaxButton submitNewButton = new AjaxValidationStyleSubmitButton("newSubmit", profileForm){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				target.addComponent(profileFeedback);
				getRepositoryService().saveUser(user);
				form.info(getString("message.success"));
				//clear User, hide Profile and enable Username
				user = new UserDataView();
				profileForm.add(new SimpleAttributeModifier("class","formHide"));
				lockUsername = false;
				target.addComponent(userForm);
				target.addComponent(profileForm);
				
				//Clear and set focus on User Name Text Field
				target.appendJavascript(
					"$('"+username.getMarkupId()+"').clear();"+
					"$('"+username.getMarkupId()+"').focus();");
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form) {
				super.onError(target, form);
				target.addComponent(profileFeedback);

			}
        };
        profileForm.add(submitNewButton);
		
		
	}
	
    private IRepositoryService getRepositoryService(){
    	return ((IRepositoryServiceProvider)getApplication()).getRepositoryService();
    }
    
    private boolean sendEmail(String username, String password, String subject){
		
        IEmailSender email = getEmailSender();
        String body = getEmailBody(username,password);
        return email.sendEmail(subject, body);
    }
    
    private String getEmailBody(String arg0, String arg1){
        Object[] args = {arg0,arg1};
        String body = MessageFormat.format(getEmailtemplate(),args);

        return body;
    }
    
    private String getEmailtemplate(){
        StringBuffer sb = new StringBuffer("Welcome...").append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        sb.append("Your new account has been created.").append(System.getProperty("line.separator"));
        sb.append("User Name : {0}").append(System.getProperty("line.separator"));
        sb.append("Password : {1}").append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
        sb.append("Please change your password after you have Logged In using the 'User Profile Page'.");
        return sb.toString();
    }
    
    protected IEmailSender getEmailSender(){
    	return ((IEmailServiceProvider)getApplication()).getEmailSender();
    }
}
