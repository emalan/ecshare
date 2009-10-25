package com.madalla.webapp.security;

public interface IPasswordAuthenticator {

    public boolean authenticate(String user, char[] password);
    
    public boolean authenticate(String user, String password);
    
}
