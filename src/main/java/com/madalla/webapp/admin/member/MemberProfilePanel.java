package com.madalla.webapp.admin.member;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.madalla.bo.member.MemberData;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.admin.pages.AdminPanelLink;
import com.madalla.wicket.form.AjaxValidationForm;
import com.madalla.wicket.form.AjaxValidationRequiredTextField;

public class MemberProfilePanel extends CmsPanel{
	private static final long serialVersionUID = 1L;

	public class MemberForm extends AjaxValidationForm<MemberData> {
		private static final long serialVersionUID = 1L;
		
		public MemberForm(String id, IModel<MemberData> model){
			super(id, model);
			
			FeedbackPanel emailFeedback;
			add(emailFeedback = new FeedbackPanel("emailFeedback"));
			TextField<String> emailField = new AjaxValidationRequiredTextField("email", emailFeedback);
            emailField.add(EmailAddressValidator.getInstance());
            add(emailField);

			FeedbackPanel firstNameFeedback;
			add(firstNameFeedback = new FeedbackPanel("firstNameFeedback"));
			add(new AjaxValidationRequiredTextField("firstName", firstNameFeedback));
			
			FeedbackPanel lastNameFeedback;
			add(lastNameFeedback = new FeedbackPanel("lastNameFeedback"));
			add(new AjaxValidationRequiredTextField("lastName", lastNameFeedback));
			
			add(new TextField<String>("companyName"));
			
			add(new CheckBox("authorized"));
			
		}

		@Override
		protected void onSubmit(AjaxRequestTarget target) {
			getRepositoryService().saveMember(getModelObject());
			info(getString("message.success"));
		}
	}
	public MemberProfilePanel(final String id, final MemberData entry) {
		super(id);
		
		add(new AdminPanelLink("memberAdminLink", MemberAdminPanel.class));
		
		add(new MemberForm("memberForm", new CompoundPropertyModel<MemberData>(entry)));
	}

}
