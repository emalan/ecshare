package com.madalla.webapp.security;

public interface IPasswordAuthenticator {

    boolean authenticate(String user, char[] password);

    boolean authenticate(String user, String password);

    UserLoginTracker getUserLoginTracker(String user);

}
