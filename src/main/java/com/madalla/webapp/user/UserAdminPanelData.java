package com.madalla.webapp.user;

import java.io.Serializable;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.emalan.cms.IDataService;
import org.emalan.cms.ISessionDataService;
import org.emalan.cms.bo.SiteData;
import org.emalan.cms.bo.security.UserData;
import org.emalan.cms.bo.security.UserSiteData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.madalla.service.ApplicationService;
import com.madalla.util.security.SecurityUtils;
import com.madalla.webapp.security.IAuthenticator;

public abstract class UserAdminPanelData implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(UserAdminPanelData.class);

    public static UserAdminPanelData newInstance(final ApplicationService applicationService,
            final ISessionDataService sessionService) {
        return new UserAdminPanelData() {

            private static final long serialVersionUID = 1L;

            @Override
            public ApplicationService getApplicationService() {
                return applicationService;
            }

            @Override
            public ISessionDataService getSessionService() {
                return sessionService;
            }
        };
    }

    final IModel<? extends List<UserData>> userListModel = new LoadableDetachableModel<List<UserData>>() {
        private static final long serialVersionUID = 1L;

        @Override
        protected List<UserData> load() {
            return getDataService().getUsers();
        }

    };

    public List<SiteData> getAvailableSites() {
        return getDataService().getSiteEntries();
    }

    public IModel<? extends List<UserData>> getUsersModel() {
        return new LoadableDetachableModel<List<UserData>>() {
            private static final long serialVersionUID = 1L;

            @Override
            protected List<UserData> load() {
                return getDataService().getUsers();
            }

        };
    }

    public IAuthenticator getUserAuthenticator() {
        return getApplicationService().getUserAuthenticator();
    }

    public SiteData getSiteData() {
        return getDataService().getSiteData();
    }

    public UserData getUserData(final String username) {
        return getDataService().getUser(username);
    }

    public List<UserSiteData> getUserSiteData(UserData user) {
        return getDataService().getUserSiteEntries(user);
    }

    public boolean isUserSite(final UserData userData) {
        return getDataService().isUserSite(userData);
    }

    public String resetPassword(UserDataView user) {
        String password = SecurityUtils.getGeneratedPassword();
        log.debug("resetPassword - username=" + user.getName() + "password=" + password);
        user.setPassword(SecurityUtils.encrypt(password));
        saveUserData(user, user.getRequiresAuth(), null);
        return password;
    }

    public void saveUserData(UserData userData, Boolean auth, List<SiteData> sites) {
        UserData dest = getDataService().getUser(userData.getName());
        BeanUtils.copyProperties(userData, dest);
        log.debug("saveUserData - " + dest);
        getSessionService().validateTransaction(dest);
        getSessionService().logTransaction(dest);
        getDataService().saveDataObject(dest);
        if (sites != null) {
            log.debug("saveUserData - save Site Entries");
            getDataService().saveUserSiteEntries(dest, sites, auth == null ? false : auth.booleanValue());
        }
    }

    private IDataService getDataService() {
        return getApplicationService().getRepositoryService();
    }

    public abstract ApplicationService getApplicationService();
    
    public abstract ISessionDataService getSessionService(); 
}
