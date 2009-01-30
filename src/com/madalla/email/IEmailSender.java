package com.madalla.email;

public interface IEmailSender {

    abstract boolean sendEmail();
    abstract boolean sendEmail(String subject, String body);
    abstract boolean sendUserEmail(String subject, String body, String email, String name);

}