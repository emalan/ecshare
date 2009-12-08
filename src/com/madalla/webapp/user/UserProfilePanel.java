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
import com.madalla.bo.security.UserData;
import com.madalla.webapp.AdminPage;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.login.aware.LoginAwareAdminLink;
import com.madalla.webapp.pages.SecurePasswordPage;
import com.madalla.webapp.pages.UserAdminPage;
import com.madalla.webapp.pages.UserPasswordPage;
import com.madalla.webapp.panel.CmsPanel;
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
		
		final String username = ((CmsSession)getSession()).getUsername();
		log.debug("Retrieved User name from Session. username="+username);
        UserData user = getRepositoryService().getUser(username);
        log.debug(user);
		
        //TODO why does this panel switch not work
		//User admin link 
//        add(new AdminPanelLink("UserAdmin", true){
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			public void onClick() {
//				replaceWith(new UserAdminPanel(getId()));
//			}
//        	
//        });
        add(new LoginAwareAdminLink("UserAdmin", UserAdminPage.class, true, true, true));
        
		//User Change Link - secure or not depending on authenticator
		IAuthenticator authenticator = getRepositoryService().getUserAuthenticator();
		SiteData siteData = getRepositoryService().getSiteData();
		
		if (siteData.getSecurityCertificate() && authenticator.requiresSecureAuthentication(username)){
			
			PageParameters parameters = new PageParameters("user=" + username);
			add(new BookmarkablePageLink<String>("PasswordChange", SecurePasswordPage.class, parameters));
			
		} else {
			add(new Link<Object>("PasswordChange"){
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					AdminPage page = new UserPasswordPage(username,"");
					page.setReturnPage(((AdminPage)getPage()).getReturnPage());
					setResponsePage(page);
				}
				
			});
		}
		add(JavascriptPackageResource.getHeaderContribution(Scriptaculous.PROTOTYPE));
		add(Css.CSS_FORM);

        //****************
        //Profile Section
        
		//Heading
		add(new Label("profileHeading",getString("heading.profile", new Model<UserData>(user) )));
		
		//Form
		add(new ProfileForm("profileForm", new CompoundPropertyModel<UserData>(user)));
		
	}
	
}
