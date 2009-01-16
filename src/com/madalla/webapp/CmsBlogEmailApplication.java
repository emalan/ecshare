package com.madalla.webapp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.WicketRuntimeException;

import com.madalla.email.IEmailSender;
import com.madalla.email.IEmailServiceProvider;

/**
 * Adds Email functionality so you can use the {@link com.madalla.webapp.email.EmailFormPanel}
 * 
 * @author Eugene Malan
 *
 */
public abstract class CmsBlogEmailApplication extends CmsApplication implements IEmailServiceProvider {

	private final static Log log = LogFactory.getLog(CmsBlogEmailApplication.class);
	private IEmailSender emailSender;
	
	protected void init() {
		super.init();
    	if (emailSender == null){
    		log.fatal("Email Sender is not configured Correctly.");
    		throw new WicketRuntimeException("Service is not configured Correctly.");
    	}
	}
	
    public IEmailSender getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(IEmailSender emailSender) {
        this.emailSender = emailSender;
    }

}
