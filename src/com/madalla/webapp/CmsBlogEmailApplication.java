package com.madalla.webapp;

import com.madalla.service.email.IEmailSender;
import com.madalla.service.email.IEmailServiceProvider;

public abstract class CmsBlogEmailApplication extends CmsBlogApplication implements IEmailServiceProvider {

	private IEmailSender emailSender;
	
    public IEmailSender getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(IEmailSender emailSender) {
        this.emailSender = emailSender;
    }

}
