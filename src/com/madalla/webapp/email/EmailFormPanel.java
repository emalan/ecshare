package com.madalla.webapp.email;

import java.text.MessageFormat;

import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.form.AjaxFormValidatingBehavior;
import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponentLabel;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.protocol.http.ClientProperties;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.request.WebClientInfo;
import org.apache.wicket.util.value.ValueMap;
import org.apache.wicket.validation.validator.EmailAddressValidator;

import com.madalla.service.email.IEmailSender;

public abstract class EmailFormPanel extends Panel {
    private static final long serialVersionUID = -1643728343421366820L;
    private String subject;
    
    public EmailFormPanel(String id, String subject) {
        super(id);
        this.subject = subject;
        Form emailForm = new EmailForm("emailForm"); 
        add(emailForm);
        //AjaxFormValidatingBehavior.addToAllFormComponents(emailForm,"onBlur");
    }

    public class EmailForm extends Form {
        private static final long serialVersionUID = -2684823497770522924L;
        private final ValueMap properties = new ValueMap();
        private FeedbackPanel feedback;
        
        public EmailForm(String id) {
            super(id);
            
            TextField name = new RequiredTextField("name",new PropertyModel(properties,"name"));
            add(name);
            add(new FormComponentLabel("nameLabel",name));
            add(new FeedbackPanel("nameFeedback", new ComponentFeedbackMessageFilter(name)));
            
            RequiredTextField email = new RequiredTextField("email",new PropertyModel(properties,"email"));
            email.add(EmailAddressValidator.getInstance());
            add(email);
            add(new FormComponentLabel("emailLabel",email));
            add(new FeedbackPanel("emailFeedback",new ComponentFeedbackMessageFilter(email)));
            
            TextArea comment = new TextArea("comment",new PropertyModel(properties,"comment"));
            add(comment);
            
            add(new FormComponentLabel("commentLabel",comment));
            add(new FeedbackPanel("commentFeedback", new ComponentFeedbackMessageFilter(comment)));
            
            feedback = new FeedbackPanel("feedback", new ComponentFeedbackMessageFilter(this));
            add(feedback);
            
        }

        protected void onSubmit() {
            IEmailSender email = getEmailSender();
            String body = getEmailBody(properties.getString("name"),properties.getString("email"),properties.getString("comment"));
            boolean result = email.sendEmail(subject, body);
            if (result){
                info("Email sent successfully");
            } else {
                error("Failed to send email!");
            }
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

    }
    protected abstract IEmailSender getEmailSender();
    
}
