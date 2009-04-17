package com.madalla.webapp.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.StringAutoCompleteRenderer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.springframework.beans.BeanUtils;

import com.madalla.bo.SiteData;
import com.madalla.bo.security.UserData;
import com.madalla.bo.security.UserSiteData;
import com.madalla.email.IEmailSender;
import com.madalla.email.IEmailServiceProvider;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.email.EmailFormatter;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.wicket.form.AjaxValidationStyleRequiredTextField;
import com.madalla.wicket.form.AjaxValidationStyleSubmitButton;
import com.madalla.wicket.form.AjaxValidationStyleSubmitLink;

public class UserAdminPanel extends Panel {

	private static final long serialVersionUID = 9027719184960390850L;
	private static final Log log = LogFactory.getLog(UserAdminPanel.class);

	private UserDataView user = new UserDataView() ;
	private boolean lockUsername = false;
	private TextField usernameField;
	private List<SiteData> sites = new ArrayList<SiteData>() ;
	private List<SiteData> sitesChoices ;

	public class NewUserForm extends Form {
		private static final long serialVersionUID = 9033980585192727266L;

		public NewUserForm(String id) {
			super(id);
			
			//User Name Field
			usernameField = new RequiredTextField("username",
					new PropertyModel(user,"name")) {

				private static final long serialVersionUID = 1L;

				@Override
				protected void onBeforeRender() {
					if (lockUsername) {
						setEnabled(false);
					} else {
						setEnabled(true);
					}
					super.onBeforeRender();
				}

			};
			usernameField.setOutputMarkupId(true);
			usernameField.add(new AutoCompleteBehavior(StringAutoCompleteRenderer.INSTANCE) {

				private static final long serialVersionUID = 1L;

				@Override
				protected Iterator<?> getChoices(String input) {
					if (Strings.isEmpty(input)) {
						return Collections.EMPTY_LIST.iterator();
					}
					List<String> choices = new ArrayList<String>(10);

					List<UserData> list = getRepositoryService().getUsers();
					for(UserData data : list){
						if (data.getName().toLowerCase().startsWith(input.toLowerCase())){
							choices.add(data.getName());
							if (choices.size() == 10) {
								break;
							}
						}
					}
					return choices.iterator();
				}

			});
			
			//Validation
			usernameField.add(new AbstractValidator(){
				private static final long serialVersionUID = 1L;
				@Override
				protected void onValidate(IValidatable validatable) {
					String value = (String) validatable.getValue();
					if (StringUtils.isEmpty(value)){
						error(validatable,"error.required");
					}else if (!StringUtils.isAlphanumeric(value)){
						error(validatable,"error.alphanumeric");
					} else {
						if (value.length() <= 4){
							error(validatable, "error.length");
						}
					}
				}
				
			});
			
			add(usernameField);
		}
	}

	public class ProfileForm extends Form {
		private static final long serialVersionUID = -2684823497770522924L;

		public ProfileForm(String id) {
			super(id);

			FeedbackPanel emailFeedback = new FeedbackPanel("emailFeedback");
			add(emailFeedback);
			TextField email = new AjaxValidationStyleRequiredTextField("email",
					new PropertyModel(user, "email"), emailFeedback);
			email.add(EmailAddressValidator.getInstance());
			add(email);

			add(new TextField("firstName", new PropertyModel(user, "firstName")));
			add(new TextField("lastName", new PropertyModel(user, "lastName")));
			add(new CheckBox("adminMode", new PropertyModel(user, "admin")));
			sitesChoices = getRepositoryService().getSiteEntries();
			add(new CheckBoxMultipleChoice("site", new Model((Serializable) sites) ,
					sitesChoices, new ChoiceRenderer("name")));
			
		}
	}

	public UserAdminPanel(String id, Class<? extends Page> returnPage) {

		super(id);

		add(HeaderContributor.forJavaScript(Scriptaculous.PROTOTYPE));
		add(Css.CSS_FORM);

		add(new PageLink("returnLink", returnPage));

		final Form userForm = new NewUserForm("userForm");
		userForm.setOutputMarkupId(true);
		add(userForm);

		final FeedbackPanel userFeedback = new ComponentFeedbackPanel(
				"userFeedback", userForm);
		userFeedback.setOutputMarkupId(true);
		userForm.add(userFeedback);

		// User edit form
		final Form profileForm = new ProfileForm("profileForm");
		profileForm.setOutputMarkupId(true);
		profileForm.add(new SimpleAttributeModifier("class", "formHide"));
		add(profileForm);

		//Select or Create New User
		AjaxSubmitLink newUserSubmit = new AjaxValidationStyleSubmitLink(
				"userSubmit", userForm) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				target.addComponent(userFeedback);

				retrieveUserData(user.getName());
				log.debug("onSubmit - "+user);
				profileForm.add(new SimpleAttributeModifier("class","formShow"));
				target.addComponent(profileForm);
				lockUsername = true;
				target.addComponent(userForm);
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form form) {
				super.onError(target, form);
				target.addComponent(userFeedback);
			}

			@Override
			protected final void onBeforeRender() {
				if (lockUsername) {
					setEnabled(false);
				} else {
					setEnabled(true);
				}
				super.onBeforeRender();
			}
		};
		newUserSubmit.setOutputMarkupId(true);
		userForm.add(newUserSubmit);
		
		usernameField.add(new AttributeModifier("onchange", true, new Model(
				"document.getElementById('"+ newUserSubmit.getMarkupId()
				+ "').onclick();return false;")));
		

		//Welcome Email
		final Label welcomeFeedback = new Label("welcomeFeedback", new Model(""));
		welcomeFeedback.setOutputMarkupId(true);
		profileForm.add(welcomeFeedback);
		
		final AjaxLink welcomeLink = new IndicatingAjaxLink("welcomeLink"){
			private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
          		if(sendEmail("Welcome Email", getEmailWelcomeMessage())){
               		welcomeFeedback.setDefaultModelObject(getString("welcome.success"));
           		} else {
           			welcomeFeedback.setDefaultModelObject(getString("welcome.fail"));
           		}
         		target.addComponent(welcomeFeedback);
            }

            @Override
			protected void onBeforeRender() {
				if(StringUtils.isEmpty(user.getEmail())){
					setEnabled(false);
				} else {
					setEnabled(true);
				}
				super.onBeforeRender();
			}

		};
		profileForm.add(welcomeLink);

		//Reset Password 
		final Label resetFeedback = new Label("resetFeedback", new Model(""));
		resetFeedback.setOutputMarkupId(true);
		profileForm.add(resetFeedback);
		
        final AjaxLink resetLink = new IndicatingAjaxLink("resetLink"){
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
                if (sendEmail("Reset Password", getEmailResetMessage())){
                	resetFeedback.setDefaultModelObject(getString("reset.success"));
                } else {
                	resetFeedback.setDefaultModelObject(getString("reset.fail"));
                }
                target.addComponent(resetFeedback);
            }
            
            @Override
			protected void onBeforeRender() {
				if(StringUtils.isEmpty(user.getEmail())){
					setEnabled(false);
				} else {
					setEnabled(true);
				}
				super.onBeforeRender();
			}

        };
        profileForm.add(resetLink);

		final FeedbackPanel profileFeedback = new ComponentFeedbackPanel(
				"profileFeedback", profileForm);
		profileFeedback.setOutputMarkupId(true);
		profileForm.add(profileFeedback);

		//Submit Button for just Saving
		final AjaxButton submitButton = new AjaxValidationStyleSubmitButton(
				"profileSubmit", profileForm) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);
				target.addComponent(profileFeedback);
				saveUserData(user);
				form.info(getString("message.success"));
				target.addComponent(welcomeLink);
				target.addComponent(resetLink);
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form form) {
				super.onError(target, form);
				target.addComponent(profileFeedback);

			}
		};
		profileForm.add(submitButton);

		//Submit for saving and resetting for starting another
		AjaxButton submitNewButton = new AjaxValidationStyleSubmitButton(
				"newSubmit", profileForm) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				super.onSubmit(target, form);

				target.addComponent(profileFeedback);
				saveUserData(user);
				form.info(getString("message.success"));

				// clear User, hide Profile and enable Username
				profileForm.add(new SimpleAttributeModifier("class", "formHide"));
				lockUsername = false;
				target.addComponent(userForm);
				target.addComponent(profileForm);
				resetUserData();

				// Clear and set focus on User Name Text Field
                target.appendJavascript("$('" + usernameField.getMarkupId() + "').clear();" 
                        + "$('" + usernameField.getMarkupId() + "').focus();");
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form form) {
				super.onError(target, form);
				target.addComponent(profileFeedback);

			}
		};
		profileForm.add(submitNewButton);
		
	}
	
	private boolean sendEmail(String subject, String message){
        String emailBody = EmailFormatter.getUserEmailBody(user, message);
        return getEmailSender().sendUserEmail("Reset Password", emailBody, user.getEmail(), user.getFirstName());
	}
	
	private String resetPassword(){
        String password = SecurityUtils.getGeneratedPassword();
        log.debug("resetPassword - username="+user.getName() + "password="+ password);
        user.setPassword(SecurityUtils.encrypt(password));
        saveUserData(user);
        return password;
	}
	
	private void retrieveUserData(String username){
		UserData src = getRepositoryService().getUser(username);
		BeanUtils.copyProperties(src, user);
		List<UserSiteData> list = getRepositoryService().getUserSiteEntries(src);
		for(UserSiteData userSite : list){
			for(SiteData site : sitesChoices){
				if (site.getName().equals(userSite.getName())){
					sites.add(site);
				}
			}
		}
	}
	
	private void saveUserData(UserData userData){
		UserData dest = getRepositoryService().getUser(userData.getName());
		BeanUtils.copyProperties(userData, dest);
		getRepositoryService().saveUser(dest);
		getRepositoryService().saveUserSiteEntries(dest, sites);
	}
	
	private void resetUserData(){
	    user.setName("");
	    sites.clear();
	}
	
	private IRepositoryService getRepositoryService() {
		return ((IRepositoryServiceProvider) getApplication())
				.getRepositoryService();
	}

	private String getEmailWelcomeMessage() {
		String password = resetPassword();
		StringBuffer sb = new StringBuffer("Your new account has been created.")
			.append(System.getProperty("line.separator"));
		sb.append("User Name : " + user.getName()).append(System.getProperty("line.separator"));
		sb.append("Password : " + password).append(System.getProperty("line.separator"))
			.append(System.getProperty("line.separator"));
		sb.append("Please change your password after you have Logged In using the 'User Profile Page'.");
		return sb.toString();
	}
	
	private String getEmailResetMessage() {
		String password = resetPassword();
		StringBuffer sb = new StringBuffer("Your password has been reset.")
			.append(System.getProperty("line.separator"));
		sb.append("User Name : " + user.getName()).append(System.getProperty("line.separator"));
		sb.append("New Password : " + password).append(System.getProperty("line.separator"))
			.append(System.getProperty("line.separator"));
		sb.append("Please change your password after you have Logged In using the 'User Profile Page'.");
		return sb.toString();
	}

	protected IEmailSender getEmailSender() {
		return ((IEmailServiceProvider) getApplication()).getEmailSender();
	}
}
