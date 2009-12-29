package com.madalla.webapp.email;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
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
import com.madalla.email.IEmailServiceProvider;
import com.madalla.service.IDataService;
import com.madalla.service.IDataServiceProvider;
import com.madalla.util.captcha.CaptchaUtils;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.wicket.form.AjaxValidationForm;
import com.madalla.wicket.form.AjaxValidationRequiredTextField;

public class EmailFormPanel extends CmsPanel {
    private static final long serialVersionUID = -1643728343421366820L;

    private Log log = LogFactory.getLog(this.getClass());
    //private String imagePass = CaptchaUtils.randomString(4, 6);
    private Integer first = CaptchaUtils.randomInteger(1, 20);
    private Integer second = CaptchaUtils.randomInteger(1, 12);
    
    private final String subject ;
    
    public class EmailForm extends AjaxValidationForm<Object> {
        private static final long serialVersionUID = -2684823497770522924L;
        
        private String name ;
        private String email ;
        private String comment ;
      //private final CaptchaImageResource captchaImageResource;
        
        public EmailForm(String id) {
            super(id);
            
            //captchaImageResource = new CaptchaImageResource(imagePass);
    
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
            
            FeedbackPanel passwordFeedback = new FeedbackPanel("passwordFeedback");
            add(passwordFeedback);
            RequiredTextField<String> passwordField = new AjaxValidationRequiredTextField("password", new Model<String>(""), passwordFeedback);
            add(passwordField);
            passwordField.add(new AbstractValidator<String>(){
				private static final long serialVersionUID = 2572094991300700912L;
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
//                    captchaImageResource.invalidate();
                }
            });
            
        }

		@Override
		protected void onSubmit() {
			if (!isSubmitted()) {
				log.debug("onSumit called- sending email.");
				if (sendEmail(name,email,comment)) {
					info("Email sent successfully");
				} else {
					error("Failed to send email!");
				}
			}
		}

		@Override
		protected void onSubmit(AjaxRequestTarget target) {
			
			if (sendEmail(name, email, comment)){
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
    
    private boolean sendEmail(String name, String email, String comment){
    	logEmail(name, email, comment);
		SiteData site = getSiteData();
        IEmailSender emailSender = getEmailSender();
        String body = getEmailBody(name, email, comment);

        if (StringUtils.isEmpty(site.getAdminEmail())){
            return emailSender.sendEmail(subject, body);
        } else {
            return emailSender.sendUserEmail(subject, body, site.getAdminEmail(), "Site Admin");
        }
    }
    
    private void logEmail(String name, String email, String comment){
    	try {
    		IDataService service = ((IDataServiceProvider)getApplication()).getRepositoryService();
    		service.createEmailEntry(name, email, comment);
    	} catch (DataAccessException e){
    		log.error("Data Access Exception while logging email.", e);
    	}
    }
    
    private String getEmailBody(String from, String email, String comment){
        Object[] args = {from,email,(comment == null)?"":comment};
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
        StringBuffer sb = new StringBuffer("Email sent from emalan.com website...").append(System.getProperty("line.separator"));
        sb.append("From: {0} ({1})").append(System.getProperty("line.separator"));
        sb.append("Comment: {2}").append(System.getProperty("line.separator"));
        return sb.toString();
    }
    
    protected IEmailSender getEmailSender(){
    	return ((IEmailServiceProvider)getApplication()).getEmailSender();
    }
    
    protected SiteData getSiteData(){
        return getRepositoryService().getSiteData();
    }
    
}
