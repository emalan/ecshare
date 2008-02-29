package com.madalla.service.email;

public interface IEmailSender {

    public abstract boolean sendEmail();
    public abstract boolean sendEmail(String subject, String body);

}