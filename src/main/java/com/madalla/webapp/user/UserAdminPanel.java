package com.madalla.webapp.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AbstractAutoCompleteTextRenderer;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteBehavior;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.IAutoCompleteRenderer;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.MapModel;
import org.apache.wicket.util.string.Strings;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.emalan.cms.IDataService;
import org.emalan.cms.bo.SiteData;
import org.emalan.cms.bo.security.UserData;
import org.emalan.cms.bo.security.UserSiteData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.madalla.email.IEmailSender;
import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.CmsApplication;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.wicket.animation.Animator;
import com.madalla.wicket.animation.AnimatorSubject;
import com.madalla.wicket.form.AjaxValidationRequiredTextField;
import com.madalla.wicket.form.AjaxValidationSubmitButton;
import com.madalla.wicket.form.ValidationStyleBehaviour;

public class UserAdminPanel extends CmsPanel {

	private static final long serialVersionUID = 9027719184960390850L;
	private static final Logger log = LoggerFactory.getLogger(UserAdminPanel.class);

	private boolean lockUsername = false;
	UserData userData = null;

	private List<SiteData> sites = new ArrayList<SiteData>() ;
	private List<SiteData> sitesChoices ;

	public class ProfileForm extends Form<UserDataView> {
		private static final long serialVersionUID = -2684823497770522924L;

		public ProfileForm(String id, IModel<UserDataView> model) {
			super(id, model);

			FeedbackPanel emailFeedback = new FeedbackPanel("emailFeedback");
			add(emailFeedback);
			TextField<String> email = new AjaxValidationRequiredTextField("email", emailFeedback);
			email.add(new ValidationStyleBehaviour());
			email.add(EmailAddressValidator.getInstance());
			add(email);

			add(new TextField<String>("firstName").setOutputMarkupId(true));
			add(new TextField<String>("lastName").setOutputMarkupId(true));
			add(new CheckBox("admin").setOutputMarkupId(true));
			add(new CheckBox("requiresAuth").setOutputMarkupId(true));

			sitesChoices = getRepositoryService().getSiteEntries();
			@SuppressWarnings({ "unchecked", "rawtypes" })
			IModel<Collection<SiteData>> siteModel = new Model((Serializable) sites);
			add(new CheckBoxMultipleChoice<SiteData>("site", siteModel ,
					sitesChoices, new ChoiceRenderer<SiteData>("name")).setOutputMarkupId(true));
		}

	}

	public UserAdminPanel(String id) {

		super(id);

		///////////////////////
		// Main User edit form
		///////////////////////

		final UserDataView userView = new UserDataView();

		final MarkupContainer profileBlock = new WebMarkupContainer("profileBlock");
		profileBlock.setOutputMarkupId(true);
		add(profileBlock);
		final Form<UserDataView> profileForm = new ProfileForm("profileForm", new CompoundPropertyModel<UserDataView>(userView));
		profileForm.setOutputMarkupId(true);
		profileBlock.add(profileForm);

		////////////////////////////////////
		// User autocomplete select/new user
		////////////////////////////////////

		Form<Object> userForm = new StatelessForm<Object>("userForm");
		add(userForm);

		final TextField<String> usernameField = new RequiredTextField<String>("name",new Model<String>("")) {
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
		userForm.add(usernameField);
		usernameField.setOutputMarkupId(true);
		IAutoCompleteRenderer<UserData> autoCompleteRenderer = new AbstractAutoCompleteTextRenderer<UserData>(){
			private static final long serialVersionUID = 1L;

			@Override
			protected String getTextValue(UserData object) {
				return object.getName();
			}

		};
		usernameField.add(new AutoCompleteBehavior<UserData>(autoCompleteRenderer) {

			private static final long serialVersionUID = 1L;

			@Override
			protected Iterator<UserData> getChoices(String input) {
				if (Strings.isEmpty(input)) {
					List<UserData> s = Collections.emptyList();
					return s.iterator();
				}
				List<UserData> choices = new ArrayList<UserData>(10);

				List<UserData> list = getRepositoryService().getUsers();
				for(UserData data : list){
					if (data.getName().toLowerCase().startsWith(input.toLowerCase())){
						choices.add(data);
						if (choices.size() == 10) {
							break;
						}
					}
				}
				return choices.iterator();
			}

		});

		//Validation
		usernameField.add(new AbstractValidator<String>(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void onValidate(IValidatable<String> validatable) {
				String value = validatable.getValue();
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

		final FeedbackPanel userFeedback = new ComponentFeedbackPanel("formFeedback", usernameField);
		userFeedback.setOutputMarkupId(true);
		userForm.add(userFeedback);

		/////////////////
		// User Select
		/////////////////
		final IModel<? extends List<UserData>> userListModel = new LoadableDetachableModel<List<UserData>>(){
			private static final long serialVersionUID = 1L;

			@Override
			protected List<UserData> load() {
				return getRepositoryService().getUsers();
			}

		};

		final ListChoice<UserData> userSelect = new ListChoice<UserData>("userSelect", new PropertyModel<UserData>(this, "userData"), userListModel,
				new IChoiceRenderer<UserData>(){
					private static final long serialVersionUID = 1L;

					public Object getDisplayValue(UserData object) {
						return object.getName() + " ("
							+ StringUtils.defaultIfEmpty(object.getFirstName(),"...")
							+ " " + StringUtils.defaultIfEmpty(object.getLastName(),"...") + ")";
					}

					public String getIdValue(UserData object, int index) {
						return object.getName();
					}

		});
		userSelect.setNullValid(false);
		userSelect.setMaxRows(7);
		add(userSelect);

		//////////////////////////
		// User selection actions
		//////////////////////////

		// animator to open/close profile form
		final Animator hideShowProfile = new Animator().addSubjects(AnimatorSubject.slideOpen(profileBlock.getMarkupId(), 37));
		add(hideShowProfile);

		AjaxButton newUserSubmit = new AjaxButton("createUser"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				String userName = (String) usernameField.getDefaultModelObject();
				log.debug("selected user: "+ userName);
				populateUserData(userName, userView);

				target.addComponent(profileForm);
				lockUsername = true;
				target.addComponent(usernameField);
				target.addComponent(userFeedback);
				target.addComponent(userSelect);
				target.appendJavaScript(hideShowProfile.seekToEnd());

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.addComponent(userFeedback);
			}

		};
		newUserSubmit.setOutputMarkupId(true);
		userForm.add(newUserSubmit);
		userForm.setDefaultButton(newUserSubmit);

		usernameField.add(new AttributeModifier("onchange", true, new Model<String>(
				"document.getElementById('"+ newUserSubmit.getMarkupId()
				+ "').onclick();return false;")));

		userSelect.add(new AjaxFormComponentUpdatingBehavior("onchange"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				log.debug("selected user=" + userData);

				userView.clear();
				sites.clear();

				profileForm.clearInput();
				populateUserData(userData, userView);
				target.appendJavaScript("$('" + usernameField.getMarkupId() + "').value='"+userData.getName()+"';");
				lockUsername = true;

				target.addComponent(profileForm);
				target.addComponent(usernameField);
				target.addComponent(getComponent());
				target.addComponent(userFeedback);

				target.appendJavaScript(hideShowProfile.seekToEnd());

			}

		});

		////////////////////////
		// Welcome Email Button
		////////////////////////

		final Label welcomeFeedback = new Label("welcomeFeedback", new Model<String>(""));
		welcomeFeedback.setOutputMarkupId(true);
		profileForm.add(welcomeFeedback);

		final AjaxLink<String> welcomeLink = new IndicatingAjaxLink<String>("welcomeLink"){
			private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
            	String message = formatUserMessage("message.new", userView);
          		if(sendEmail("Welcome Email", message, userView)){
               		welcomeFeedback.setDefaultModelObject(getString("welcome.success"));
           		} else {
           			welcomeFeedback.setDefaultModelObject(getString("welcome.fail"));
           		}
         		target.addComponent(welcomeFeedback);
            }

            @Override
			protected void onBeforeRender() {
				if(StringUtils.isEmpty(userView.getEmail())){
					setEnabled(false);
				} else {
					setEnabled(true);
				}
				super.onBeforeRender();
			}

		};
		profileForm.add(welcomeLink);

		////////////////////////
		// Reset Password Button
		////////////////////////

		final Label resetFeedback = new Label("resetFeedback", new Model<String>(""));
		resetFeedback.setOutputMarkupId(true);
		profileForm.add(resetFeedback);

        final AjaxLink<String> resetPasswordLink = new IndicatingAjaxLink<String>("resetLink"){
            private static final long serialVersionUID = 1L;

            @Override
            public void onClick(AjaxRequestTarget target) {
            	String message = formatUserMessage("message.reset", userView);
                if (sendEmail("Reset Password", message, userView)){
                	resetFeedback.setDefaultModelObject(getString("reset.success"));
                } else {
                	resetFeedback.setDefaultModelObject(getString("reset.fail"));
                }
                target.addComponent(resetFeedback);
            }

            @Override
			protected void onBeforeRender() {
				if(StringUtils.isEmpty(userView.getEmail())){
					setEnabled(false);
				} else {
					setEnabled(true);
				}
				super.onBeforeRender();
			}

        };
        profileForm.add(resetPasswordLink);

		final FeedbackPanel profileFeedback = new ComponentFeedbackPanel(
				"profileFeedback", profileForm);
		profileFeedback.setOutputMarkupId(true);
		profileForm.add(profileFeedback);

		////////////////////////////////
		// Submit Button for just Saving
		////////////////////////////////

		final AjaxButton submitButton = new AjaxValidationSubmitButton(
				"profileSubmit", profileForm) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				target.addComponent(profileFeedback);
				saveUserData(userView, userView.getRequiresAuth());
				form.info(getString("message.success"));
				target.addComponent(welcomeLink);
				target.addComponent(resetPasswordLink);
			}

			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.addComponent(profileFeedback);

			}
		};
		profileForm.add(submitButton);
		profileForm.setDefaultButton(submitButton);

		///////////////////
		// Reset form data
		///////////////////

		add(new AjaxButton("resetForm",profileForm) {
			private static final long serialVersionUID = 1L;

			@Override
			public void onSubmit(AjaxRequestTarget target, Form<?> form) {

				lockUsername = false;
				userData = null;

				//reset form data
				form.modelChanging();
				userView.clear();
				sites.clear();
				form.modelChanged();
				form.clearInput();

				usernameField.setDefaultModelObject("");
				usernameField.clearInput();

				target.addComponent(usernameField);
				target.addComponent(userFeedback);
				target.addComponent(userSelect);
				target.addComponent(form);

				// Clear usernamefield and reset form
                target.appendJavaScript(hideShowProfile.seekToBegin() + "$('" + usernameField.getMarkupId() + "').clear();"
                        + "$('"+form.getMarkupId()+"').reset();");

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				
			}

		}.setDefaultFormProcessing(false));

	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderJavaScriptReference(Scriptaculous.PROTOTYPE);
		response.renderCSSReference(Css.CSS_FORM);

	}

	private boolean sendEmail(String subject, String message, UserDataView user){
        return getApplicationService().sendUserEmail(subject, message, user.getEmail(), user.getFirstName(), true);
	}

	private String resetPassword(UserDataView user){
        String password = SecurityUtils.getGeneratedPassword();
        log.debug("resetPassword - username="+user.getName() + "password="+ password);
        user.setPassword(SecurityUtils.encrypt(password));
        saveUserData(user, user.getRequiresAuth());
        return password;
	}

	//User gets created if there is not one
	private void populateUserData(String username, UserDataView user){
		userData = getRepositoryService().getUser(username);
		populateUserData(userData, user);
	}

	private void populateUserData(UserData src, UserDataView user){
		BeanUtils.copyProperties(src, user);
		List<UserSiteData> userSites = getRepositoryService().getUserSiteEntries(src);
		for(UserSiteData userSite : userSites){
			Boolean requiresAuth = userSite.getRequiresAuthentication();
			if (requiresAuth != null && requiresAuth.booleanValue()) {
				user.setRequiresAuth(true);
			}
			for(SiteData site : sitesChoices){
				if (site.getName().equals(userSite.getName())){
					sites.add(site);
				}
			}
		}
	}

	private void saveUserData(UserData userData, Boolean auth){
		UserData dest = getRepositoryService().getUser(userData.getName());
		BeanUtils.copyProperties(userData, dest);
		log.debug("saveUserData - " + dest);
		saveData(dest);
		log.debug("saveUserData - save Site Entries");
		getRepositoryService().saveUserSiteEntries(dest, sites, auth == null? false : auth.booleanValue());
	}

	private String formatUserMessage(String key, UserDataView user) {
		IDataService service = getRepositoryService();
		SiteData site = service.getSiteData();
    	UserData userData = service.getUser(user.getName());

		Map<String, String> map = new HashMap<String, String>();
		map.put("firstName", StringUtils.defaultString(user.getFirstName()));
		map.put("lastName", StringUtils.defaultString(user.getLastName()));
		map.put("username", user.getName());
		map.put("password", resetPassword(user));
		map.put("siteName", StringUtils.defaultString(site.getSiteName()));
		String url = StringUtils.defaultString(site.getUrl());
		map.put("url", url );
		map.put("description", StringUtils.defaultString(site.getMetaDescription()));

		MapModel<String, String> model = new MapModel<String,String>(map);
		String message = getString(key, model);

    	if (service.isUserSite(userData) && StringUtils.isNotEmpty(url)){
    		IAuthenticator authenticator = getRepositoryService().getUserAuthenticator();
    		if (site.getSecurityCertificate() && authenticator.requiresSecureAuthentication(userData.getName())){
    			map.put("passwordChangePage", CmsApplication.SECURE_PASSWORD);
    		} else {
    			map.put("passwordChangePage", CmsApplication.PASSWORD);
    		}
    		message = message + getString("message.password", model);
    	}
    	List<UserSiteData> sites = service.getUserSiteEntries(userData);
    	if (sites.size() > 0){
    		message = message + getString("message.sites");
    	}
		for (UserSiteData siteData : sites){
			SiteData accessSite = service.getSite(siteData.getName());
			message = message + getString("message.site", new Model<SiteData>(accessSite));
		}
		message = message + getString("message.note") + getString("message.closing");
    	log.debug("formatMessage - " + message);
		return message;
	}

	public void setUserData(UserData userData){
		this.userData = userData;
	}
	public UserData getUserData() {
		return userData;
	}
}
