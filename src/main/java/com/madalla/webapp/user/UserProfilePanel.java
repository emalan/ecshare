package com.madalla.webapp.user;


import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.emalan.cms.bo.SiteData;
import org.emalan.cms.bo.security.IUser;
import org.emalan.cms.bo.security.UserData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static final Logger log = LoggerFactory.getLogger(UserProfilePanel.class);

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
    
	public UserProfilePanel(String id) {
		super(id);
	}


	@Override
	public void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(Scriptaculous.PROTOTYPE));
		response.render(CssHeaderItem.forReference(Css.CSS_FORM));
	}
	
	@Override
	protected void onBeforeRender() {
		UserData user = getSessionDataService().getUser();
        log.debug(user.toString());

		//User Change Link - secure or not depending on authenticator
		IAuthenticator authenticator = getApplicationService().getUserAuthenticator();
		SiteData siteData = getRepositoryService().getSiteData();

		final String username = user.getName();
		if (siteData.getSecurityCertificate() && authenticator.requiresSecureAuthentication(username)){

			final PageParameters parameters = new PageParameters();
			parameters.add("user", username);
			addOrReplace(new BookmarkablePageLink<String>("PasswordChange", SecurePasswordPage.class, parameters));

		} else {
			addOrReplace(new Link<Object>("PasswordChange"){
				private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(new UserPasswordPage(username,""));
				}

			});
		}

		addOrReplace(new Label("profileHeading", getString("heading.profile", new Model<IUser>(user))));
		addOrReplace(new ProfileForm("profileForm", new CompoundPropertyModel<UserData>(user)));

		super.onBeforeRender();
	}

}
