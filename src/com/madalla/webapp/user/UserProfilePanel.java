package com.madalla.webapp.user;


import static com.madalla.webapp.PageParams.RETURN_PAGE;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.madalla.bo.security.UserData;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.login.aware.LoggedinBookmarkablePageLink;
import com.madalla.webapp.pages.SecurePasswordPage;
import com.madalla.webapp.pages.UserAdminPage;
import com.madalla.webapp.pages.UserPasswordPage;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.wicket.form.AjaxValidationStyleRequiredTextField;
import com.madalla.wicket.form.AjaxValidationStyleSubmitButton;

public class UserProfilePanel extends CmsPanel{

	private static final long serialVersionUID = 9027719184960390850L;
	private static final Log log = LogFactory.getLog(UserProfilePanel.class);
	
	private UserData user;
	
    public class ProfileForm extends Form<Object> {
        private static final long serialVersionUID = -2684823497770522924L;
        
        public ProfileForm(String id) {
            super(id);
            
            FeedbackPanel emailFeedback = new FeedbackPanel("emailFeedback");
            add(emailFeedback);
            TextField<String> email = new AjaxValidationStyleRequiredTextField("email",new PropertyModel<String>(user,"email"), emailFeedback);
            email.add(EmailAddressValidator.getInstance());
            add(email);
            
            add(new TextField<String>("firstName", new PropertyModel<String>(user,"firstName")));
            add(new TextField<String>("lastName", new PropertyModel<String>(user,"lastName")));
        }
    }

	public UserProfilePanel(String id, String returnPage){
		
		super(id);
		
		String username = ((CmsSession)getSession()).getUsername();
		log.debug("Retrieved User name from Session. username="+username);
        user = getRepositoryService().getUser(username);
        log.debug(user);
		
		//User admin link
		PageParameters params = new PageParameters(RETURN_PAGE + "=" + returnPage+", user=" + username);
		add(new LoggedinBookmarkablePageLink("UserAdmin", UserAdminPage.class, params, true, true, true).setAutoEnable(true));
		
		//User Change Link - secure or not depending on authenticator
		IAuthenticator authenticator = getRepositoryService().getUserAuthenticator();
		if (authenticator.requiresSecureAuthentication(username)){
			add(new LoggedinBookmarkablePageLink("PasswordChange", SecurePasswordPage.class, params, false, false, false).setAutoEnable(true));
		} else {
			add(new LoggedinBookmarkablePageLink("PasswordChange", UserPasswordPage.class, params, false, false, false).setAutoEnable(true));
		}
		add(JavascriptPackageResource.getHeaderContribution(Scriptaculous.PROTOTYPE));
		add(Css.CSS_FORM);

        //****************
        //Profile Section
        
		//Heading
		add(new Label("profileHeading",getString("heading.profile", new Model<UserData>(user) )));
		
		//Form
		Form<Object> profileForm = new ProfileForm("profileForm");
		profileForm.setOutputMarkupId(true);
		add(profileForm);
		
		final FeedbackPanel profileFeedback = new ComponentFeedbackPanel("profileFeedback",profileForm);
		profileFeedback.setOutputMarkupId(true);
		profileForm.add(profileFeedback);
		
        AjaxButton submitButton = new AjaxValidationStyleSubmitButton("profileSubmit", profileForm){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				target.addComponent(profileFeedback);

				saveData(user);
				form.info(getString("message.success"));
				//setResponsePage(this.findPage());
			}
			
			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				super.onError(target, form);
				target.addComponent(profileFeedback);

			}
        };
        profileForm.add(submitButton);
		
		
	}
	
}
