package com.madalla.webapp.pages;

import org.apache.wicket.markup.html.link.Link;

import com.madalla.webapp.cms.IContentAdmin;

/**
 * Admin Link that will switch admin Panels
 * 
 * Usage - implement onClick() method like such...
 * <pre>
 * 	@ Override
 *	public void onClick() {
 *		getPage().replace(new TranslatePanel(ID, nodeName, contentId));
 *	}
 * </pre>
 * @author Eugene Malan
 *
 */
public abstract class AdminPanelLink extends Link<Object> {

	private static final long serialVersionUID = 1L;
	protected static final String ID = "adminPanel";
	
	final private boolean admin;
	
	/**
	 * Main Constructor
	 * 
	 * @param id - wicket id
	 * @param admin - set true if this is a super admin only link
	 */
	public AdminPanelLink(String id, final boolean admin){
		super(id);
		this.admin = admin;
	}
	
    public AdminPanelLink(String id) {
		this(id, false);
	}

	@Override
	public boolean isEnabled() {
		// If we're auto-enabling
		if (getAutoEnable())
		{
			// TODO disable link if Panel already active
			if (false) return false;
		}
		IContentAdmin session = (IContentAdmin) getSession();
		if (admin) {
			return session.isSuperAdmin();
        } else {
            return session.isLoggedIn();
        }
	}

	
}
