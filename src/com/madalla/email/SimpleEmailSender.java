package com.madalla.email;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;

public class SimpleEmailSender implements IEmailSender, Serializable {
	private static final long serialVersionUID = 1628736470390933607L;
	private static final Log log = LogFactory.getLog(SimpleEmailSender.class);
    private String emailHost;
    private String emailFromName;
    private String emailFromEmail;
    private String emailToName;
    private String emailToEmail;
    private String mailAuthName;
    private String mailAuthPassword;
    
    
    public SimpleEmailSender(){
    }
    
    public boolean sendUserEmail(String subject, String body, String userEmail, String userName){
    	return sendUserEmail(new SimpleEmail(), subject, body, userEmail, userName);
    }
    
    public boolean sendUserHtmlEmail(String subject, String body, String userEmail, String userName){
    	return sendUserEmail(new HtmlEmail(), subject, body, userEmail, userName);
    }
    
    private boolean sendUserEmail(Email email, String subject, String body, String userEmail, String userName){
    	log.debug("sendUserEmail - userEmail:"+userEmail+" userName:"+userName+" subject:"+subject );
    	if (StringUtils.isEmpty(userEmail)){
    		log.error("sendUserEmail - The parameter userEmail cannot be empty. A valid email needs to be supplied");
    		return false;
    	}
    	sendEmailUsingCommonsMail(email, subject, body, false, "", "");
    	return sendEmailUsingCommonsMail(email, subject, body, true, userEmail, userName);
    }
    
    public boolean sendEmail(String subject, String body){
    	log.debug("sendEmail - subject:"+subject);
        return sendEmailUsingCommonsMail(new SimpleEmail(), subject, body, false, "", "");
    }
    
    public boolean sendEmail(){
        return sendEmail("com.emalan.service.email.SimpleEmailSender - no subject", 
                "com.emalan.service.email.SimpleEmailSender - no body");
    }
    
    private void init(Email email) throws EmailException {
        email.setHostName(emailHost);
        email.setAuthentication(mailAuthName, mailAuthPassword);
        email.setFrom(emailFromEmail, emailFromName);
        email.setDebug(true);
    }
    
    private boolean sendEmailUsingCommonsMail(Email email, String subject, String body, boolean user, String userEmail, String userName){
        try {
        	init(email);
        	if (user){
        		email.addTo(userEmail, userName);
        	} else {
        		email.addTo(emailToEmail, emailToName);
        	}
            email.setSubject(subject);
            email.setMsg(body);
            log.debug("Sending email."+this);
            email.send();
        } catch (EmailException e) {
            log.error("Exception while sending email from emalancom.",e);
            log.warn("Email not sent:" + this);
            return false;
        }
        return true;
    }
    
    public void setEmailFromEmail(String emailFromEmail) {
        this.emailFromEmail = emailFromEmail;
    }

    public void setEmailFromName(String emailFromName) {
        this.emailFromName = emailFromName;
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    public void setEmailToEmail(String emailToEmail) {
        this.emailToEmail = emailToEmail;
    }

    public void setEmailToName(String emailToName) {
        this.emailToName = emailToName;
    }
    
    public String toString() {
    	return ReflectionToStringBuilder.toString(this);
    }

    public void setMailAuthName(String mailAuthName) {
        this.mailAuthName = mailAuthName;
    }

    public void setMailAuthPassword(String mailAuthPassword) {
        this.mailAuthPassword = mailAuthPassword;
    }

}
