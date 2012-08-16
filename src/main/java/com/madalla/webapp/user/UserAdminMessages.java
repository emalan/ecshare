package com.madalla.webapp.user;

import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.model.Model;
import org.emalan.cms.bo.SiteData;
import org.emalan.cms.bo.security.UserData;
import org.emalan.cms.bo.security.UserSiteData;

import com.madalla.webapp.CmsApplication;
import com.madalla.webapp.security.IAuthenticator;

public class UserAdminMessages {

    public static String formatUserMessage(final String key, final UserData user, final String password,
            final SiteData site, final List<SiteData> sites, final boolean userSite,
            final IAuthenticator authenticator) {

        final ResourceBundle messages = ResourceBundle.getBundle(UserAdminMessages.class.getName());

        Object[] messageArguments = { StringUtils.defaultString(user.getFirstName()),
                StringUtils.defaultString(user.getLastName()), user.getName(), password };

        final MessageFormat messageFormat = new MessageFormat(messages.getString(key));
        StringBuffer sb = messageFormat.format(messageArguments, new StringBuffer(), null);

        if (userSite && StringUtils.isNotEmpty(site.getUrl())) {
            final MessageFormat passwordFormat = new MessageFormat(messages.getString("message.password"));
            String url = StringUtils.defaultString(site.getUrl());
            final String page;
            if (site.getSecurityCertificate() && authenticator.requiresSecureAuthentication(user.getName())) {
                page = CmsApplication.SECURE_PASSWORD;
            } else {
                page = CmsApplication.PASSWORD;
            }
            Object[] passwordArgs = {url, page, user.getName(), password};
            passwordFormat.format(passwordArgs, sb, null);
        }
        
        if(sites.size() > 0) {
            sb.append(messages.getString("message.sites"));
        }
        for (SiteData entry : sites) {
            final MessageFormat siteFormat = new MessageFormat(messages.getString("message.site"));
            Object[] args = {entry.getSiteName(), entry.getUrl(), entry.getMetaDescription()};
            siteFormat.format(args, sb, null);
        }
        sb.append(messages.getString("message.note")).append(messages.getString("message.closing"));
  
        return sb.toString();

    }
}
