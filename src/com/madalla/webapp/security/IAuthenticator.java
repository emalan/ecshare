package com.madalla.webapp.security;

public interface IAuthenticator {
    
    public boolean authenticate(String user, char[] password);
    
    public boolean authenticate(String user, String password);
    
    public boolean authenticate(String user);
    
    public boolean requiresSecureAuthentication(String user);
 
}