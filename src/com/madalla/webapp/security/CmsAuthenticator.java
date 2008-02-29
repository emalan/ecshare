package com.madalla.webapp.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CmsAuthenticator implements IAuthenticator {
    private static final Log log = LogFactory.getLog(CmsAuthenticator.class);

    private Credentials credentials;

    public Credentials getCredentials() {
        return credentials;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }
    
    public boolean authenticate(String user, String password) {
        boolean ret = false;
        try {
            String pwd = String.valueOf(credentials.getPassword());
            if (credentials.getUserID().equalsIgnoreCase(user) && pwd.equals(password)){
                ret = true;
            } else {
                log.warn("authenticate - CMS logon failed. username="+user);
            }
        } catch (RuntimeException e) {
            log.error("authenticate - Exception while authenticating CMS logon.",e);
        }
        return ret;
    }

    public boolean authenticate(String user, char[] password) {
        return false;
    }
    
}
