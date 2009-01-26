package com.madalla.webapp.user;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualInputValidator;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.madalla.bo.security.UserData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.wicket.form.ValidationStyleBehaviour;
import com.madalla.wicket.form.ValidationStyleRequiredTextField;

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
            existingFeedback.setOutputMarkupId(true);
            add(existingFeedback);
            TextField existingPassword = new PasswordTextField("existingPassword", new PropertyModel(properties,"existingPassword"));
            existingPassword.setOutputMarkupId(true);
            existingPassword.add(new ValidationStyleBehaviour());
            add(existingPassword);

            TextField newPassword = new PasswordTextField("newPassword",
                    new PropertyModel(properties,"newPassword"));
            newPassword.setOutputMarkupId(true);
            newPassword.add(new ValidationStyleBehaviour());
            add(newPassword);
            
            TextField confirmPassword = new PasswordTextField("confirmPassword",
                    new PropertyModel(properties,"confirmPassword"));
            confirmPassword.setOutputMarkupId(true);
            confirmPassword.add(new ValidationStyleBehaviour());
            add(confirmPassword);
            
            add(new EqualInputValidator(newPassword, confirmPassword));
            
        }
    }

	
    public class ProfileForm extends Form {
        private static final long serialVersionUID = -2684823497770522924L;
        
        //private final CaptchaImageResource captchaImageResource;
        
        public ProfileForm(String id) {
            super(id);
            
            FeedbackPanel emailFeedback = new FeedbackPanel("emailFeedback");
            add(emailFeedback);
            TextField email = new ValidationStyleRequiredTextField("email",new PropertyModel(user,"email"), emailFeedback);
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
        
        AjaxButton passwordSubmit = new IndicatingAjaxButton("passwordSubmit", passwordForm){
            private static final long serialVersionUID = 1L;

            @Override
            protected void onSubmit(final AjaxRequestTarget target, Form form) {
                log.debug("Ajax onsubmit called.");
                form.visitChildren(new Component.IVisitor() {
                    public Object component(Component component){
                        log.debug("formVisitor="+component);
                        if (component instanceof FormComponent) {
                            FormComponent formComponent = (FormComponent) component;
                            if (formComponent.isValid()){
                                target.addComponent(formComponent);
                            }
                        } else if (component instanceof ComponentFeedbackPanel){
                            log.debug("Ajax submit - adding feedback to target.");
                            ComponentFeedbackPanel feedback = (ComponentFeedbackPanel) component;
                            target.addComponent(feedback);
                        }
                        return null;
                    }
                });
                target.addComponent(passwordFeedback);
                IAuthenticator authenticator = getRepositoryService().getUserAuthenticator();
                if(authenticator.authenticate(user.getName(), SecurityUtils.encrypt(properties.getString("existingPassword")))){
                    user.setPassword(SecurityUtils.encrypt(properties.getString("newPassword")));
                    getRepositoryService().saveUser(user);
                    form.info(getString("message.success"));
                } else {
                    form.error(getString("message.fail"));
                }
                //setResponsePage(this.findPage());
            }
            
            @Override
            protected void onError(final AjaxRequestTarget target, Form form) {
                log.debug("Ajax onerror called");
                target.addComponent(passwordFeedback);
                form.visitChildren(new Component.IVisitor() {
                    public Object component(Component component) {
                        log.debug("formVisitor="+component);
                        if (component instanceof FormComponent) {
                            FormComponent formComponent = (FormComponent) component;
                            if (!formComponent.isValid()){
                                target.addComponent(formComponent);
                                log.debug("Component is invalid. Component MarkupId="+formComponent.getMarkupId()+". Message is " +formComponent.getFeedbackMessage().getMessage());
                            }
                        } else if (component instanceof ComponentFeedbackPanel){
                            log.debug("Ajax onerror - adding feedback to target.");
                            ComponentFeedbackPanel feedback = (ComponentFeedbackPanel) component;
                            target.addComponent(feedback);
                        }
                        return null;
                    }
                });

            }
        };
        passwordForm.add(passwordSubmit);

        
        
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
		
        AjaxButton submitButton = new IndicatingAjaxButton("profileSubmit", profileForm){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				log.debug("Ajax onsubmit called.");
				target.addComponent(profileFeedback);
				getRepositoryService().saveUser(user);
				form.info(getString("message.success"));
				//setResponsePage(this.findPage());
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form form) {
				log.debug("Ajax onerror called");
				target.addComponent(profileFeedback);
	           	form.visitChildren(new Component.IVisitor() {
					public Object component(Component component) {
	           			log.debug("formVisitor="+component);
	           			if (component instanceof FormComponent) {
	           				FormComponent formComponent = (FormComponent) component;
	           				if (!formComponent.isValid()){
	           					target.addComponent(formComponent);
	           					log.debug("Component is invalid. Component MarkupId="+formComponent.getMarkupId()+". Message is " +formComponent.getFeedbackMessage().getMessage());
	           				}
	           			} else if (component instanceof ComponentFeedbackPanel){
	           				log.debug("Ajax onerror - adding feedback to target.");
	           				ComponentFeedbackPanel feedback = (ComponentFeedbackPanel) component;
	           				target.addComponent(feedback);
	           			}
	           			return null;
					}
	           	});

			}
        };
        profileForm.add(submitButton);
		
		
	}
	
    private IRepositoryService getRepositoryService(){
    	return ((IRepositoryServiceProvider)getApplication()).getRepositoryService();
    }
}
