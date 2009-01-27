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
import org.apache.wicket.markup.html.form.TextField;
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
import com.madalla.wicket.form.AjaxValidationStyleRequiredTextField;
import com.madalla.wicket.form.AjaxValidationStyleSubmitButton;

public class UserAdminPanel extends Panel{

	private static final long serialVersionUID = 9027719184960390850L;
	private static final Log log = LogFactory.getLog(UserAdminPanel.class);
	
	private UserData user;
	private final ValueMap properties = new ValueMap();
	
    public class NewUserForm extends Form {
        private static final long serialVersionUID = 9033980585192727266L;

        
        public NewUserForm(String id) {
            super(id);
            
            FeedbackPanel usernameFeedback = new FeedbackPanel("usernameFeedback");
            add(usernameFeedback);
            TextField username = new AjaxValidationStyleRequiredTextField("username", 
            		new PropertyModel(properties,"username"), usernameFeedback);
            add(username);
            
            FeedbackPanel emailFeedback = new FeedbackPanel("emailFeedback");
            add(emailFeedback);
            TextField email = new AjaxValidationStyleRequiredTextField("email",new PropertyModel(user,"email"), emailFeedback);
            email.add(EmailAddressValidator.getInstance());
            add(email);
            
            add(new TextField("firstName", new PropertyModel(user,"firstName")));
            add(new TextField("lastName", new PropertyModel(user,"lastName")));
            
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

	public UserAdminPanel(String id, Class<? extends Page> returnPage){
		
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
        Form newUserForm = new NewUserForm("userForm");
        newUserForm.setOutputMarkupId(true);
        
        add(newUserForm);
        
        final FeedbackPanel userFeedback = new ComponentFeedbackPanel("userFeedback",newUserForm);
        userFeedback.setOutputMarkupId(true);
        newUserForm.add(userFeedback);
        
        AjaxButton newUserSubmit = new AjaxValidationStyleSubmitButton("userSubmit", newUserForm){
        	private static final long serialVersionUID = 1L;
        	
            @Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				target.addComponent(userFeedback);
                
				String password = SecurityUtils.getGeneratedPassword();
                UserData newUser = getRepositoryService().getNewUser(
                		properties.getKey("username"), SecurityUtils.encrypt(password));
                newUser.setEmail(properties.getString("email"));
                newUser.setFirstName(properties.getString("firstName"));
                newUser.setLastName(properties.getString("lastName"));
                getRepositoryService().saveUser(newUser);
                
                //TODO email user
                log.debug("New User created." + user);
                
                form.info(getString("message.success"));
			}

            @Override
            protected void onError(final AjaxRequestTarget target, Form form) {
            	super.onError(target, form);
            	target.addComponent(userFeedback);
            }
        };
        newUserForm.add(newUserSubmit);
        newUserForm.add(new AttributeModifier("onSubmit", true, new Model("document.getElementById('" + newUserSubmit.getMarkupId() + "').onclick();return false;")));
        
        
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
