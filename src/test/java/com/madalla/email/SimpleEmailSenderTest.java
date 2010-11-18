package com.madalla.email;

import junit.framework.TestCase;

public class SimpleEmailSenderTest extends TestCase {

    public void testEmailSender() {

        SimpleEmailSender emailSender = new SimpleEmailSender() {
			private static final long serialVersionUID = 1L;

			protected String getSubject() {
                return "Test subject";
            }

            protected String getBody() {
                return "Test body";
            }
        };
        emailSender.setEmailHost("smtp1.dnsmadeeasy.com");
        emailSender.setMailAuthName("emalan");
        emailSender.setMailAuthPassword("madalla");
        emailSender.setEmailFromEmail("webmaster@madalla.com");
        emailSender.setEmailToEmail("eugene@emalan.com");
                
        emailSender.sendEmail();
    }

}
