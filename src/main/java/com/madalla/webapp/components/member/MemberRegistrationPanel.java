package com.madalla.webapp.components.member;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.util.MapModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.emalan.cms.bo.SiteData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.bo.member.MemberData;
import com.madalla.webapp.CmsApplication;
import com.madalla.webapp.CmsSession;
import com.madalla.wicket.form.AjaxValidationForm;
import com.madalla.wicket.form.AjaxValidationRequiredTextField;

public class MemberRegistrationPanel extends AbstractMemberPanel{
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	public class MemberRegistrationForm extends AjaxValidationForm<MemberData> {
		private static final long serialVersionUID = 1L;

		public MemberRegistrationForm(String id, IModel<MemberData> memberModel) {
			super(id, memberModel);

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

			FeedbackPanel memberIdFeedback;
			add(memberIdFeedback = new FeedbackPanel("memberIdFeedback"));
			TextField<String> memberIdField;
			add(memberIdField = new AjaxValidationRequiredTextField("memberId", memberIdFeedback));
			memberIdField.add(new AbstractValidator<String>(){
				private static final long serialVersionUID = 1L;

				@Override
				protected void onValidate(IValidatable<String> validatable) {
					String memberId = validatable.getValue();
					if (memberExists(memberId)) {
						validatable.error(new ValidationError().addMessageKey("message.invalidMemberID"));
					}
				}

			});

		}

		@Override
		protected void onSubmit(AjaxRequestTarget target) {
			log.debug("submit - " + getModelObject());

			MemberData member = getModelObject();

			if (!saveMemberData(member)){
				error(getString("message.fail.save"));
				return;
			}

			if (sendRegistrationEmail( member)){
                info(getString("message.success"));
                this.reset(target);
                setModelObject(instanciateMember());
            } else {
                error(getString("message.fail.email"));
            }

		}

	}

	public MemberRegistrationPanel(String id) {
		super(id);

		Form<MemberData> form;
		add(form = new MemberRegistrationForm("regForm", new CompoundPropertyModel<MemberData>(instanciateMember())));
		form.setEnabled(!CmsSession.get().getMemberSession().isSignedIn());
	}

	private boolean memberExists(String memberId){
		return getRepositoryService().isMemberExist(memberId);
	}

    protected String getEmailBody(final MemberData member){
    	Map<String, String> map = new HashMap<String, String>();
    	SiteData site = getRepositoryService().getSiteData();
    	map.put("siteName", site.getSiteName());
    	map.put("firstName", StringUtils.defaultString(member.getFirstName()));
		map.put("lastName", StringUtils.defaultString(member.getLastName()));
		map.put("companyName", StringUtils.defaultString(member.getCompanyName()));
		map.put("memberId", member.getMemberId());
		map.put("password", resetPassword(member));
		String url = StringUtils.defaultString(site.getUrl());
		map.put("url", url );
    	map.put("passwordChangePage", CmsApplication.MEMBER_PASSWORD);

		MapModel<String, String> values = new MapModel<String, String>(map);
		String message = getString("email.registration", values);

		message = message + getString("message.password", values);

		message = message + getString("message.note") + getString("message.closing");

		log.debug("formatMessage - " + message);
    	return message;
    }


}
