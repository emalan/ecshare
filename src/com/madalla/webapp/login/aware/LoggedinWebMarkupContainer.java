package com.madalla.webapp.login.aware;

import org.apache.wicket.markup.html.WebMarkupContainer;

import com.madalla.webapp.cms.IContentAdmin;

public class LoggedinWebMarkupContainer extends WebMarkupContainer {

	private static final long serialVersionUID = 1L;
	final private boolean admin;
	
	public LoggedinWebMarkupContainer(String id, boolean admin) {
		super(id);
		this.admin = admin;
	}
	
    @Override
    protected void onBeforeRender() {
		IContentAdmin session = (IContentAdmin) getSession();
		if (admin ){
            setEnabled(session.isCmsAdminMode());
        } else {
            setEnabled(session.isLoggedIn());
        }
        super.onBeforeRender();
    }

}
