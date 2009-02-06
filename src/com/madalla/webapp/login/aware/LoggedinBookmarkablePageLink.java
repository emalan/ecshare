package com.madalla.webapp.login.aware;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import com.madalla.webapp.cms.IContentAdmin;

public class LoggedinBookmarkablePageLink extends BookmarkablePageLink{
	private static final long serialVersionUID = 1L;
	final private boolean admin;
	
	public LoggedinBookmarkablePageLink(String id, Class<?> pageClass, PageParameters parameters,
			final boolean admin){
		super(id, pageClass, parameters);
		this.admin = admin;
	}
    
	protected final void onBeforeRender(){
		IContentAdmin session = (IContentAdmin) getSession();
		if (admin ){
            setEnabled(session.isCmsAdminMode());
        } else {
            setEnabled(session.isLoggedIn());
        }
        super.onBeforeRender();
    }
}
