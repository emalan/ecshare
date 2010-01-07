package com.madalla.webapp.login.aware;

import com.madalla.webapp.admin.AdminPage;
import com.madalla.webapp.cms.IContentAdmin;
import com.madalla.webapp.pages.LinkAdmin;

/**
 * Admin Page Link that will enable/disable based on your wether you are logged in.
 * 
 * @author Eugene Malan
 *
 */
public class LoginAwareAdminLink extends LinkAdmin {

	private static final long serialVersionUID = 1L;
	
	final private boolean admin;
	final private boolean hide;
	final private boolean superAdmin;
	
	/**
	 * Calls main constructor
	 */
	public LoginAwareAdminLink(String id, Class<? extends AdminPage> adminPageClass,
			final boolean admin) {
		this(id, adminPageClass, admin, false);
	}
	
	/**
	 * Calls main constructor
	 */
	public LoginAwareAdminLink(String id, Class<? extends AdminPage> adminPageClass,
			final boolean admin, final boolean hide){
		this(id, adminPageClass, admin, hide, false);
	}
	
	/**
	 * Main Constructor
	 * 
	 * @param id - wicket id
	 * @param adminPageClass - Admin Page Class
	 * @param admin - set true if this is an admin only link
	 * @param hide - set true if you want to hide the link if not enabled
	 * @param superAdmin - set true if this is a super admin only link - getting cheesy - time for user roles
	 */
	public LoginAwareAdminLink(String id, Class<? extends AdminPage> adminPageClass,
			final boolean admin, final boolean hide, final boolean superAdmin){
		super(id, adminPageClass);
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
