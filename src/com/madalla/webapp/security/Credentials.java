package com.madalla.webapp.security;

public class Credentials implements ICredentials {

    private String userID;
    private char[] password;

    public Credentials(String userID, char password[]) {
        this.userID = userID;
        this.password = (char[])password.clone();
    }

    public String getUserID() {
        return userID;
    }

    public char[] getPassword() {
        return password;
    }

    
}
