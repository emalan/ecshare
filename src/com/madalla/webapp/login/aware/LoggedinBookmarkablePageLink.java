package com.madalla.webapp.login.aware;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import com.madalla.webapp.cms.IContentAdmin;

/**
 * Page Link that will enable/disable based on your wether you are logged in.
 * @author Eugene Malan
 *
 */
public class LoggedinBookmarkablePageLink extends BookmarkablePageLink<Page>{

	private static final long serialVersionUID = 1L;
	final private boolean admin;
	final private boolean hide;
	final private boolean superAdmin;
	
	/**
	 * @param id
	 * @param pageClass
	 * @param parameters
	 * @param admin - set true if this is an admin only link
	 */
	public LoggedinBookmarkablePageLink(String id, Class<? extends Page> pageClass, PageParameters parameters,
			final boolean admin){
		this(id, pageClass, parameters, admin, false);
	}

	/**
	 * @param id
	 * @param pageClass
	 * @param parameters
	 * @param admin - set true if this is an admin only link
	 * @param hide - set true if you want to hide the link if not enabled
	 */
	public LoggedinBookmarkablePageLink(String id, Class<? extends Page> pageClass, PageParameters parameters,
            final boolean admin, final boolean hide){
        this(id, pageClass, parameters, admin, hide, false);
    }

	/**
	 * @param id
	 * @param pageClass
	 * @param parameters
	 * @param admin - set true if this is an admin only link
	 * @param hide - set true if you want to hide the link if not enabled
	 * @param superAdmin - set true if this is a super admin only link - getting cheesy - time for user roles
	 */
	public LoggedinBookmarkablePageLink(String id, Class<? extends Page> pageClass, PageParameters parameters,
            final boolean admin, final boolean hide, final boolean superAdmin){
        super(id, pageClass, parameters);
        this.admin = admin;
        this.hide = hide;
        this.superAdmin = superAdmin;
    }
 
	protected final void onBeforeRender(){

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
        return true;
    }

    @Override
	public boolean isEnabled() {
		// If we're auto-enabling
		if (getAutoEnable())
		{
			// the link is enabled if this link doesn't link to the current page
			if (linksTo(getPage())) return false;
		}
		IContentAdmin session = (IContentAdmin) getSession();
		if (superAdmin) {
			return session.isSuperAdmin();
		} else if (admin ){
            return session.isCmsAdminMode();
        } else {
            return session.isLoggedIn();
        }
	}

}
