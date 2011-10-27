package com.madalla.webapp.components.email;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.captcha.CaptchaImageResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.springframework.dao.DataAccessException;

import com.madalla.bo.SiteData;
import com.madalla.email.IEmailSender;
import com.madalla.util.captcha.CaptchaUtils;
import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.css.Css;
import com.madalla.wicket.form.AjaxValidationForm;
import com.madalla.wicket.form.AjaxValidationRequiredTextField;

public class EmailFormPanel extends CmsPanel {
    private static final long serialVersionUID = -1643728343421366820L;

    private Logger log = LoggerFactory.getLogger(this.getClass());
    private String imagePass = CaptchaUtils.randomString(4, 6);
    private Integer first = CaptchaUtils.randomInteger(1, 20);
    private Integer second = CaptchaUtils.randomInteger(1, 12);

    private final String subject ;

    public class EmailForm extends AjaxValidationForm<Object> {
        private static final long serialVersionUID = -2684823497770522924L;

        private String name ;
        private String email ;
        private String comment ;
        private final CaptchaImageResource captchaImageResource;

        public EmailForm(String id) {
            super(id);

            captchaImageResource = new CaptchaImageResource(imagePass);

            FeedbackPanel nameFeedback = new FeedbackPanel("nameFeedback");
            add(nameFeedback);
            add(new AjaxValidationRequiredTextField("name",new PropertyModel<String>(this, "name"), nameFeedback));

            FeedbackPanel emailFeedback = new FeedbackPanel("emailFeedback");
            add(emailFeedback);
            TextField<String> emailField = new AjaxValidationRequiredTextField("email", new PropertyModel<String>(this, "email"),emailFeedback);
            emailField.add(EmailAddressValidator.getInstance());
            add(emailField);

            TextArea<String> commentField = new TextArea<String>("comment",new PropertyModel<String>(this,"comment"));
            add(commentField);

            add(new Label("captchaString", first+" + "+second+" = "));
            //add(new Image("captchaImage", captchaImageResource));

            FeedbackPanel passwordFeedback = new FeedbackPanel("passwordFeedback");
            add(passwordFeedback);
            RequiredTextField<String> passwordField = new AjaxValidationRequiredTextField("password", new Model<String>(""), passwordFeedback);
            add(passwordField);
            passwordField.add(new AbstractValidator<String>(){
				private static final long serialVersionUID = 2572094991300700912L;
				@Override
				protected void onValidate(IValidatable<String> validatable) {
                    String password = validatable.getValue();
                    try {
                        int answer = Integer.parseInt(password);
                        if (answer != first.intValue() + second.intValue()){
                        	log.debug("onValidate - entered:"+password+" should be:"+first+"+"+second);
                            IValidationError error = new ValidationError().addMessageKey("message.captcha");
                            validatable.error(error);
                        }
                    } catch (Exception e){
                    	IValidationError error = new ValidationError().addMessageKey("message.captcha.error");
                    	validatable.error(error);
                    	log.debug("password validate Exception.",e);
                    }
//                    if (!imagePass.equals(password)) {
//                    	log.debug("onValidate - entered:"+password+" should be:"+imagePass);
//                        IValidationError error = new ValidationError().addMessageKey("message.captcha");
//                        validatable.error(error);
//                        //"You entered '" + password + "' You should have entered '" + imagePass + "'"
//                    }
                    captchaImageResource.invalidate();
                }
            });

        }

		@Override
		protected void onSubmit() {
			if (!isSubmitted()) {
				log.debug("onSumit called- sending email.");
				if (sendEmail(getSiteData().getName(), name,email,comment)) {
					info("Email sent successfully");
				} else {
					error("Failed to send email!");
				}
			}
		}

		@Override
		protected void onSubmit(AjaxRequestTarget target) {

			if (sendEmail(getSiteData().getName(), name, email, comment)){
                info(getString("message.success"));
            } else {
                error(getString("message.fail"));
            }

		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

    }

    public EmailFormPanel(final String id, final String subject) {
        super(id);
        this.subject = subject;

        add(Css.CSS_FORM);

        add(new EmailForm("emailForm"));

    }

    private boolean sendEmail(final String site, final String name, final String email, final String comment){
    	logEmail(name, email, comment);
        String body = getEmailBody(site, name, email, comment);
        return sendEmail(getEmailSender(), getSiteData(), body, subject);
    }

    //to allow override
    protected boolean sendEmail(final IEmailSender emailSender, final SiteData site, final String body, final String subject){
        if (StringUtils.isEmpty(site.getAdminEmail())){
            return emailSender.sendEmail(subject, body);
        } else {
            return emailSender.sendUserEmail(subject, body, site.getAdminEmail(), "Site Admin", true);
        }
    }

    private void logEmail(String name, String email, String comment){
    	try {
    		getRepositoryService().createEmailEntry(name, email, comment);
    	} catch (DataAccessException e){
    		log.error("Data Access Exception while logging email.", e);
    	}
    }

    private String getEmailBody(String site, String from, String email, String comment){
        Object[] args = {site, from,email,(comment == null)?"":comment};
        String body = MessageFormat.format(getEmailtemplate(),args);
        RequestCycle requestCycle = getRequestCycle();
        if (requestCycle instanceof WebRequestCycle){
            WebClientInfo clientInfo = (WebClientInfo)((WebRequestCycle) requestCycle).getClientInfo();
            ClientProperties cp = clientInfo.getProperties();

            StringBuffer sb = new StringBuffer(body).append(System.getProperty("line.separator"));
            sb.append("NavigatorAppName : ").append(cp.getNavigatorAppName()).append(System.getProperty("line.separator"));
            sb.append("NavigatorAppCodeName : ").append(cp.getNavigatorAppCodeName()).append(System.getProperty("line.separator"));
            sb.append("NavigatorAppVersion : ").append(cp.getNavigatorAppVersion()).append(System.getProperty("line.separator"));
            sb.append("BrowserVersionMajor : ").append(cp.getBrowserVersionMajor()).append(System.getProperty("line.separator"));
            sb.append("BrowserVersionMinor : ").append(cp.getBrowserVersionMinor()).append(System.getProperty("line.separator"));
            body = sb.toString();
        }
        return body;
    }

    private String getEmailtemplate(){
        StringBuffer sb = new StringBuffer("Email sent from {0} website...").append(System.getProperty("line.separator"));
        sb.append("From: {1} ({2})").append(System.getProperty("line.separator"));
        sb.append("Comment: {3}").append(System.getProperty("line.separator"));
        return sb.toString();
    }

    protected SiteData getSiteData(){
        return getRepositoryService().getSiteData();
    }

}
