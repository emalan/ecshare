package com.madalla.email;

import junit.framework.TestCase;

public class SimpleEmailSenderTest extends TestCase {

    public void testEmailSender() {

        SimpleEmailSender emailSender = new SimpleEmailSender() {
            protected String getSubject() {
                return "Test subject";
            }

            protected String getBody() {
                return "Test body";
            }
        };
        emailSender.setEmailHost("server.madalla.com");
        emailSender.setEmailFromEmail("webmaster@madalla.com");
        emailSender.setEmailToEmail("eugene@emalan.com");
        emailSender.sendEmail();
    }

}
