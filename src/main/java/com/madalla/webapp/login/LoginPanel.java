/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.madalla.webapp.login;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.util.security.ICredentialHolder;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.css.Css;
import com.madalla.wicket.form.FocusOnLoadBehavior;

public abstract class LoginPanel extends CmsPanel {
	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(LoginPanel.class);

	private final Class<? extends Page> destination;

	/** Field for password. */
	private PasswordTextField password;
	private FormComponentLabel passwordLabel;

	/** Field for user name. */
	private TextField<String> username;
	private FormComponentLabel usernameLabel;

	private AjaxLink<String> unlockUser;
	private Label lockedLabel;

	/**
	 * Sign in form.
	 */
	public final class SignInForm extends Form<Void> {
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 * 
		 * @param id
		 *            id of the form component
		 */
		public SignInForm(final String id, ICredentialHolder credentials) {
			super(id);

			add(username = new TextField<String>("username", new PropertyModel<String>(credentials, "username")));
			username.setRequired(true);
			add(password = new PasswordTextField("password", new PropertyModel<String>(credentials, "password")));

			add(usernameLabel = new FormComponentLabel("usernameLabel", username));
			usernameLabel.setOutputMarkupId(true);

			add(passwordLabel = new FormComponentLabel("passwordLabel", password));
			passwordLabel.setVisibilityAllowed(true);
			passwordLabel.setOutputMarkupId(true);

			password.setRequired(false);
			password.setOutputMarkupId(true);
			password.setVisibilityAllowed(true);

		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		public void onSubmit() {
			log.trace("onSubmit");
		}
	}

	public LoginPanel(final String id, final ICredentialHolder credentials) {
		this(id, credentials, null);
	}

	/**
	 * @param id
	 *            See Component constructor
	 * @see org.apache.wicket.Component#Component(String)
	 */
	public LoginPanel(final String id, final ICredentialHolder credentials, Class<? extends Page> destination) {
		super(id);

		if (destination == null) {
			this.destination = getApplication().getHomePage();
		} else {
			this.destination = destination;
		}

		// if we have a valid populated credential then validate
		if (StringUtils.isNotEmpty(credentials.getUsername()) && StringUtils.isNotEmpty(credentials.getPassword())
				&& signIn(credentials.getUsername(), credentials.getPassword())) {

			throw new RestartResponseAtInterceptPageException(destination);

		}

		final Form<Void> form = new SignInForm("signInForm", credentials);

		add(form);

		final FeedbackPanel feedback = new FeedbackPanel("loginFeedback");
		feedback.setOutputMarkupId(true);
		form.add(feedback);

		lockedLabel = new Label("lockedLabel", new StringResourceModel("label.locked", this,
				new Model<ICredentialHolder>(credentials)));
		lockedLabel.setVisibilityAllowed(true);
		lockedLabel.setVisible(false);
		form.add(lockedLabel);

		unlockUser = new AjaxLink<String>("unlockUser") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				target.add(form);
				lockUserName(false);
				credentials.setUsername("");
			}

		};
		unlockUser.setVisibilityAllowed(true);
		form.add(unlockUser);

		// set up depending on if we have a username or not
		lockUserName(StringUtils.isNotEmpty(credentials.getUsername()));

		AjaxButton submit = new IndicatingAjaxButton("submitLink", form) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.debug("Ajax onError called");
				target.add(feedback);
				target.add(form);
				onSignInFailed(getUsername());
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				log.debug("Ajax onSubmit. userName=" + getUsername());
				target.add(form);
				preSignIn(getUsername());

				if (!isUserLocked()) {
					lockUserName(true);
				} else {
					if (signIn(getUsername(), getPassword())) {
						feedback.info(getLocalizer().getString("signInFailed", this, "Success"));
						onSignInSucceeded(target);
					} else {
						feedback.error(getLocalizer().getString("signInFailed", this, "Sign in failed"));
						target.add(feedback);
						onSignInFailed(getUsername());
					}

				}

			}

		};
		submit.setEnabled(true);

		// submit.setVisibilityAllowed(true);
		form.add(submit);
		form.add(new AttributeModifier("onSubmit", new Model<String>("document.getElementById('"
				+ submit.getMarkupId() + "').onclick();return false;")));

	}

	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		username.setLabel(new Model<String>(getString("label.name")));
		password.setLabel(new Model<String>(getString("label.password")));
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference(Css.CSS_FORM);
	}

	private void lockUserName(boolean lock) {
		username.setVisible(!lock);
		usernameLabel.setVisible(!lock);
		unlockUser.setVisible(lock);
		password.setVisible(lock);
		password.setRequired(lock);
		passwordLabel.setVisible(lock);
		lockedLabel.setVisible(lock);
		if (lock) {
			password.add(new FocusOnLoadBehavior());
		} else {
			username.add(new FocusOnLoadBehavior());
		}
	}

	private boolean isUserLocked() {
		return !username.isVisible();
	}

	/**
	 * Convenience method to access the password.
	 * 
	 * @return The password
	 */
	public String getPassword() {
		return password.getDefaultModelObjectAsString();
	}

	/**
	 * Convenience method to access the username.
	 * 
	 * @return The user name
	 */
	public String getUsername() {
		return username.getDefaultModelObjectAsString();
	}

	/**
	 * Sign in user if possible.
	 * 
	 * @param username
	 *            The username
	 * @param password
	 *            The password
	 * @return True if signin was successful
	 */
	public abstract boolean signIn(String username, String password);

	protected void preSignIn(String username) {
	    
	}

	protected void onSignInFailed(String username) {
		if (null == username) {
			log.warn("onSignInFailed - no user name.");
		} else {
			log.warn("onSignInFailed - user = " + username);
		}
	}

	protected void onSignInSucceeded() {
		// If login has been called because the user was not yet
		// logged in, then continue to the original destination,
		// otherwise to the Home page
		if (!continueToOriginalDestination()) {
			setResponsePage(destination);
		}
	}

	protected void onSignInSucceeded(AjaxRequestTarget target) {
		onSignInSucceeded();

	}

}
