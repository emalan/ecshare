package com.madalla.email;

public interface IEmailSender {

    abstract boolean sendEmail();
    abstract boolean sendEmail(String subject, String body);

}