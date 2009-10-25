package com.madalla.webapp.security;

import junit.framework.TestCase;

public class CmsAuthenticatorTest extends TestCase {
    public void testAuthenticate(){
        CmsAuthenticator authenticatorImpl = new CmsAuthenticator();
        String user = "testUser";
        String password = "testPass1";
        char[] charPassword = password.toCharArray();
        Credentials credentials = new Credentials(user,charPassword);
        authenticatorImpl.setCredentials(credentials);
        
        IPasswordAuthenticator authenticator = authenticatorImpl;
        
        assertTrue(authenticator.authenticate("testUser","testPass1"));
        assertTrue(authenticator.authenticate("TESTUSER","testPass1"));
        assertFalse(authenticator.authenticate("testUser","testPass2"));
        assertFalse(authenticator.authenticate("nottestUser","testPass1"));
        assertFalse(authenticator.authenticate("nottestUser","test"));
    }

}
