package com.madalla.webapp;

import org.apache.wicket.Request;
import org.apache.wicket.protocol.http.WebSession;

import com.madalla.webapp.cms.IContentAdmin;

public abstract class CmsSession  extends WebSession implements IContentAdmin{

    private boolean cmsAdminMode = false;
    
    public CmsSession(Request request) {
        super(request);
    }

    public boolean isCmsAdminMode() {
        return cmsAdminMode;
    }
    
    public void setCmsAdminMode(boolean cmsAdminMode) {
        this.cmsAdminMode = cmsAdminMode;
    }
}
