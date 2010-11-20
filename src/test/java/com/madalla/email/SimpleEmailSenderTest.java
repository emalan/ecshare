package com.madalla.email;

import junit.framework.TestCase;

public class SimpleEmailSenderTest extends TestCase {

    public void testEmailSender() {

        SimpleEmailSender emailSender = new SimpleEmailSender();
        emailSender.setEmailHost("smtp1.dnsmadeeasy.com");
        emailSender.setMailAuthName("emalan");
        emailSender.setMailAuthPassword("madalla");
        emailSender.setEmailFromEmail("webmaster@madalla.com");
        emailSender.setEmailToEmail("eugene@emalan.com");
                
        emailSender.sendEmail("Test subject","Test body");
    }

}
