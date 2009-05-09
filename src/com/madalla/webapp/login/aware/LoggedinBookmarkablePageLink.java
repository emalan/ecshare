package com.madalla.webapp.login.aware;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import com.madalla.webapp.cms.IContentAdmin;

public class LoggedinBookmarkablePageLink extends BookmarkablePageLink<Page>{

    private static final long serialVersionUID = 1L;
	final private boolean admin;
	final private boolean hide;
	
	public LoggedinBookmarkablePageLink(String id, Class<? extends Page> pageClass, PageParameters parameters,
			final boolean admin){
		this(id, pageClass, parameters, admin, false);
	}

	public LoggedinBookmarkablePageLink(String id, Class<? extends Page> pageClass, PageParameters parameters,
            final boolean admin, final boolean hide){
        super(id, pageClass, parameters);
        this.admin = admin;
        this.hide = hide;
    }
    
	protected final void onBeforeRender(){
		IContentAdmin session = (IContentAdmin) getSession();
		if (admin ){
            setEnabled(session.isCmsAdminMode());
        } else {
            setEnabled(session.isLoggedIn());
        }
		if (hide){
		    if (!isEnabled()){
		        setVisible(false);
		    } else {
		        setVisible(true);
		    }
		}
        super.onBeforeRender();
    }

    @Override
    protected boolean callOnBeforeRenderIfNotVisible() {
        return hide;
    }

}
