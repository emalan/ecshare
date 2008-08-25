package com.madalla.service.email;

public interface IEmailSender {

    abstract boolean sendEmail();
    abstract boolean sendEmail(String subject, String body);

}