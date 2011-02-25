package com.madalla.webapp.user;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.madalla.bo.SiteData;
import com.madalla.bo.security.IUser;
import com.madalla.bo.security.UserData;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.admin.pages.SecurePasswordPage;
import com.madalla.webapp.admin.pages.UserPasswordPage;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.webapp.security.IAuthenticator;
import com.madalla.wicket.form.AjaxValidationForm;
import com.madalla.wicket.form.AjaxValidationRequiredTextField;

public class UserProfilePanel extends CmsPanel{

	private static final long serialVersionUID = 9027719184960390850L;
	private static final Log log = LogFactory.getLog(UserProfilePanel.class);
	
    public class ProfileForm extends AjaxValidationForm<UserData> {
        private static final long serialVersionUID = -2684823497770522924L;
        
        public ProfileForm(String id, IModel<UserData> model) {
            super(id, model);
            
            FeedbackPanel emailFeedback = new FeedbackPanel("emailFeedback");
            add(emailFeedback);
            TextField<String> email = new AjaxValidationRequiredTextField("email", emailFeedback);
            email.add(EmailAddressValidator.getInstance());
            add(email);
            add(new TextField<String>("displayName"));
            add(new TextField<String>("firstName"));
            add(new TextField<String>("lastName"));
        }

		@Override
		protected void onSubmit(AjaxRequestTarget target) {
			saveData(getModelObject());
			info(getString("message.success"));
		}
    }

	public UserProfilePanel(String id){
		
		super(id);
		
		IUser user = getSessionDataService().getUser();
        log.debug(user);
		
		//User Change Link - secure or not depending on authenticator
		IAuthenticator authenticator = getRepositoryService().getUserAuthenticator();
		SiteData siteData = getRepositoryService().getSiteData();
		
		final String username = user.getName();
		if (siteData.getSecurityCertificate() && authenticator.requiresSecureAuthentication(username)){
			
			PageParameters parameters = new PageParameters("user=" + username);
			add(new BookmarkablePageLink<String>("PasswordChange", SecurePasswordPage.class, parameters));
			
		} else {
			add(new Link<Object>("PasswordChange"){
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(new UserPasswordPage(username,""));
				}
				
			});
		}
		add(JavascriptPackageResource.getHeaderContribution(Scriptaculous.PROTOTYPE));
		add(Css.CSS_FORM);

        //****************
        //Profile Section
        
		//Heading
		add(new Label("profileHeading",getString("heading.profile", new Model<IUser>(user) )));
		
		//Form
		add(new ProfileForm("profileForm", new CompoundPropertyModel<UserData>(user)));
		
	}
	
}
