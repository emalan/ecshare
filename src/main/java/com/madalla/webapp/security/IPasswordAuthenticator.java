package com.madalla.webapp.security;

import com.madalla.webapp.security.PasswordAuthenticator.UserLoginTracker;

public interface IPasswordAuthenticator {

    boolean authenticate(String user, char[] password);
    
    boolean authenticate(String user, String password);
    
    UserLoginTracker getUserLoginTracker(String user);
    
}
