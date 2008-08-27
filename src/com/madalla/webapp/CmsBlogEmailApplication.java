package com.madalla.webapp;

import com.madalla.service.email.IEmailSender;
import com.madalla.service.email.IEmailServiceProvider;

/**
 * Adds Email functionality so you can use the {@link com.madalla.webapp.email.EmailFormPanel}
 * 
 * @author Eugene Malan
 *
 */
public abstract class CmsBlogEmailApplication extends CmsBlogApplication implements IEmailServiceProvider {

	private IEmailSender emailSender;
	
    public IEmailSender getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(IEmailSender emailSender) {
        this.emailSender = emailSender;
    }

}
