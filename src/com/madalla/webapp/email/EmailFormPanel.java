package com.madalla.webapp.email;

import static com.madalla.webapp.scripts.scriptaculous.Scriptaculous.PROTOTYPE;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidationError;
import org.apache.wicket.validation.ValidationError;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.madalla.service.captcha.CaptchaUtils;
import com.madalla.service.email.IEmailSender;
import com.madalla.service.email.IEmailServiceProvider;
import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
import com.madalla.wicket.ValidationStyleBehaviour;

public class EmailFormPanel extends Panel {
    private static final long serialVersionUID = -1643728343421366820L;
    private static final CompressedResourceReference JS_PROTOTYPE = new CompressedResourceReference(Scriptaculous.class, "prototype.js");
    private static final CompressedResourceReference JS_EFFECTS = new CompressedResourceReference(Scriptaculous.class, "effects.js");

    private Log log = LogFactory.getLog(this.getClass());
    //private String imagePass = CaptchaUtils.randomString(4, 6);
    private Integer first = CaptchaUtils.randomInteger(1, 20);
    private Integer second = CaptchaUtils.randomInteger(1, 12);
    
    private final ValueMap properties = new ValueMap();
    String subject ;

    
    public class EmailForm extends Form {
        private static final long serialVersionUID = -2684823497770522924L;
        
        
        //private final CaptchaImageResource captchaImageResource;
        
        public EmailForm(String id) {
            super(id);
            
            //captchaImageResource = new CaptchaImageResource(imagePass);
    
            TextField name = new RequiredTextField("name",new PropertyModel(properties,"name"));
            name.setLabel(new Model(EmailFormPanel.this.getString("label.name")));
            FeedbackPanel nameFeedback = new ComponentFeedbackPanel("nameFeedback",name);
            nameFeedback.setMaxMessages(1);
            nameFeedback.setOutputMarkupId(true);
            add(nameFeedback);
            name.add(new ValidationStyleBehaviour());
            name.add(new CustomAjaxValidationBehavior(nameFeedback));
            add(name);
            
            RequiredTextField email = new RequiredTextField("email",new PropertyModel(properties,"email"));
            email.add(EmailAddressValidator.getInstance());
            FeedbackPanel emailFeedback = new ComponentFeedbackPanel("emailFeedback", email);
            emailFeedback.setOutputMarkupId(true);
            emailFeedback.setMaxMessages(1);
            add(emailFeedback);
            email.add(new ValidationStyleBehaviour());
            email.add(new CustomAjaxValidationBehavior(emailFeedback));
            add(email);
            
            TextArea comment = new TextArea("comment",new PropertyModel(properties,"comment"));
            add(comment);
            
            add(new Label("captchaString", first+" + "+second+" = "));
            
            RequiredTextField password = new RequiredTextField("password", new PropertyModel(properties, "password")){
				private static final long serialVersionUID = -108228073455105029L;
				protected final void onComponentTag(final ComponentTag tag) {
                        super.onComponentTag(tag);
                        // clear the field after each render
                        //tag.put("value", "");
                }
            };
            password.add(new AbstractValidator(){
				private static final long serialVersionUID = 2572094991300700912L;
				protected void onValidate(IValidatable validatable) {
                    String password = (String)validatable.getValue();
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
            FeedbackPanel passwordFeedback = new ComponentFeedbackPanel("passwordFeedback",password);
            passwordFeedback.setOutputMarkupId(true);
            passwordFeedback.setMaxMessages(1);
            add(passwordFeedback);
            password.add(new ValidationStyleBehaviour());
            password.add(new CustomAjaxValidationBehavior(passwordFeedback));
            add(password);
            
        }

		@Override
		protected void onSubmit() {
			log.debug("onSumit called- sending email.");
			//sendEmail();
		}
        

    }
    
    
    public class CustomAjaxValidationBehavior extends AjaxFormComponentUpdatingBehavior{

		private static final long serialVersionUID = 1L;
		
		private final FeedbackPanel feedbackPanel;
		
		private String validClass ;
		private String invalidClass ;
		
		public CustomAjaxValidationBehavior() {
			this("inputTextValid","inputTextError");
		}
		
		public CustomAjaxValidationBehavior(String validClass, String invalidClass){
			super("onblur");
			this.feedbackPanel = null;
			this.validClass = validClass;
			this.invalidClass = invalidClass;
		}

		public CustomAjaxValidationBehavior(FeedbackPanel feedbackPanel) {
			super("onblur");
			this.feedbackPanel = feedbackPanel;
		}

		@Override
		protected void onUpdate(AjaxRequestTarget target) {
			target.addComponent(getFormComponent());
			target.addComponent(feedbackPanel);
			target.appendJavascript(
					"$('"+feedbackPanel.getMarkupId()+"').parentNode.className = '"+validClass+"';");
		}

		@Override
		protected void onError(AjaxRequestTarget target, RuntimeException e) {
			target.addComponent(getFormComponent());
			target.addComponent(feedbackPanel);
			target.appendJavascript(
                    "new Effect.Pulsate($('" + getFormComponent().getMarkupId() + "'),{pulses:1, duration:0.3});" +
                    		"$('"+feedbackPanel.getMarkupId()+"').parentNode.className = '"+invalidClass+"';");
		}

		@Override
		public void renderHead(IHeaderResponse response) {
			super.renderHead(response);
			response.renderCSSReference(PROTOTYPE);
		}

    }

    public EmailFormPanel(final String id, final String subject) {
        super(id);
        this.subject = subject;
        
        Form form = new EmailForm("emailForm");
        form.setOutputMarkupId(true);

        final FeedbackPanel feedbackPanel = new ComponentFeedbackPanel("feedback",form);
        feedbackPanel.setOutputMarkupId(true);
        form.add(feedbackPanel);
        
        //AjaxFormValidatingBehavior.addToAllFormComponents(form,"onblur");
        //CustomAjaxFormValidationBehavior.addToAllFormComponents(form, "onkeyPress", Duration.ONE_SECOND);
        

//        form.add(new AjaxButton("submit", form){
//        	@Override
//			protected void onSubmit(AjaxRequestTarget target, Form form) {
//				log.debug("onSubmit called - Ajax sending email");
//	            if (sendEmail()){
//	                form.info("Email sent successfully");
//	            } else {
//	                form.error("Failed to send email!");
//	            }
//	            target.addComponent(feedbackPanel);
//			}
//			
//			 @Override
//			 protected void onError(final AjaxRequestTarget target, final Form form) {
//	           	log.debug("onError called");
//	           	target.addComponent(feedbackPanel);
//	           	form.visitFormComponents(new IVisitor() {
//	                public Object formComponent(IFormVisitorParticipant formVisitor) {
//	                    if (formVisitor instanceof FormComponent) {
//	                        FormComponent formComponent = (FormComponent) formVisitor;
//	                        
//	                        if (!formComponent.isValid()){
//	                             target.addComponent(formComponent);
//	                             target.appendJavascript(
//	                                "new Effect.Pulsate($('" + formComponent.getMarkupId() + "'),{pulses:1, duration:0.3});");
//	                             log.debug("Component is invalid. Component MarkupId="+formComponent.getMarkupId()+". Message is " +formComponent.getFeedbackMessage().getMessage());
//	                            
//	                         }
//	                    } else if (formVisitor instanceof ComponentFeedbackPanel){
//	                    	ComponentFeedbackPanel feedback = (ComponentFeedbackPanel) formVisitor;
//	                    	target.addComponent(feedback);
//	                    }
//	                    
//	                    return formVisitor;
//	                }
//	        	});
//
//			 }
//        });
        add(form);
        
        add(HeaderContributor.forJavaScript(JS_PROTOTYPE));
        add(HeaderContributor.forJavaScript(JS_EFFECTS));
    }
    
    private boolean sendEmail(){
		
        IEmailSender email = getEmailSender();
        String body = getEmailBody(properties.getString("name"),properties.getString("email"),properties.getString("comment"));
        return email.sendEmail(subject, body);
    }
    

    
    private String getEmailBody(String from, String email, String comment){
        Object[] args = {from,email,comment};
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
    
}
