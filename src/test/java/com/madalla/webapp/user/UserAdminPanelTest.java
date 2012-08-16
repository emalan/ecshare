package com.madalla.webapp.user;

import java.util.ArrayList;
import java.util.List;

import org.emalan.cms.bo.SiteData;
import org.emalan.cms.bo.impl.ocm.Site;
import org.emalan.cms.bo.security.UserSiteData;
import org.junit.Test;

import com.madalla.webapp.security.IAuthenticator;


public class UserAdminPanelTest {

    @Test
    public void testUserMessageFormat() {
        
        UserDataView userData = new UserDataView();
        userData.setFirstName("Eugene");
        userData.setLastName("Malan");
        userData.setName("emalan");
        userData.setPassword("encryptedPass");
        
        SiteData site = new Site();
        site.setUrl("http://localhost");
        
        List<SiteData> sites = new ArrayList<SiteData>();
        
        String result = UserAdminMessages.formatUserMessage("message.new", userData, "newpass", site, sites, true, 
                new IAuthenticator() {
            
            @Override
            public boolean requiresSecureAuthentication(String user) {
                return false;
            }
            
            @Override
            public boolean authenticate(String user) {
                return false;
            }
        });
        
        System.out.println(result);
    }
}
