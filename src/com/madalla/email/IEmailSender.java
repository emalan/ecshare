package com.madalla.email;

public interface IEmailSender {

    boolean sendEmail();
    boolean sendEmail(String subject, String body);
    boolean sendUserEmail(String subject, String body, String email, String name);
    boolean sendUserHtmlEmail(String subject, String body, String userEmail, String userName);
}