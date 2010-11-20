package com.madalla.webapp.components.member;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.springframework.dao.DataAccessException;

import com.madalla.bo.member.MemberData;
import com.madalla.db.dao.Member;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.cms.ContentPanel;
import com.madalla.wicket.form.AjaxValidationForm;
import com.madalla.wicket.form.AjaxValidationRequiredTextField;

public class MemberRegistrationPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(this.getClass());
	
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
					if (!checkMemberId(memberId)) {
						validatable.error(new ValidationError().addMessageKey("message.invalidMemberID"));
					}
				}
				
			});
			
		}

		@Override
		protected void onSubmit(AjaxRequestTarget target) {
			log.debug("submit - " + getModelObject());
			
			MemberData member = getModelObject();
			
			saveMemberData(member);
			
			if (sendEmail( member)){
                info(getString("message.success"));
            } else {
                error(getString("message.fail"));
            }
			
		}
		
	}
	
	public MemberRegistrationPanel(String id) {
		super(id);
		
		Form<MemberData> regForm;
		add(regForm = new MemberRegistrationForm("regForm", new CompoundPropertyModel<MemberData>(new Member())));
		regForm.add(new ContentPanel("regInfo"));
		
	}
	
	private boolean checkMemberId(String memberId){
		if (memberId.equals("exists")){
			return false;
		} else {
			return true;
		}
	}
	
	private boolean saveMemberData(MemberData member){
		return checkMemberId(member.getId());
	}
	
    private boolean sendEmail(final MemberData member){
    	logEmail(member.getDisplayName(), member.getEmail(), "Registration email sent.");
        String body = getEmailBody();
        return getEmailSender().sendUserEmail(getEmailSubject(), body, member.getEmail(), member.getFirstName(), true);
    }
    
    private void logEmail(String name, String email, String comment){
    	if (name.length() >= 25){
    		name = StringUtils.substring(name, 0, 23) + "..";
    	}
    	try {
    		getRepositoryService().createEmailEntry(name, email, comment);
    	} catch (DataAccessException e){
    		log.error("Data Access Exception while logging registration email.", e);
    	}
    }
    
    protected String getEmailBody(){
    	return "registration email";
    }
    
    protected String getEmailSubject(){
    	return "Registration";
    }
    
    

}
