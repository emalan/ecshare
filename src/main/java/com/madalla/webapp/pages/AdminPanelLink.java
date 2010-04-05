package com.madalla.webapp.pages;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.link.Link;

import com.madalla.webapp.cms.IContentAdmin;

/**
 * Admin Link that will switch admin Panels
 * 
 * Usage - implement onClick() method like such...
 * <pre>
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
	final private String key;
	final private String titleKey;
	
	public AdminPanelLink(final String id, final String key, String titleKey, final boolean admin){
		super(id);
		this.key = key;
		this.admin = admin;
		this.titleKey = titleKey;
	}
	
	public AdminPanelLink(final String id, final String key, final boolean admin){
		this(id, key, "", admin);
	}
	
	/**
	 * Main Constructor
	 * TODO remove this construcot
	 * 
	 * @param id - wicket id
	 * @param admin - set true if this is a super admin only link
	 */
	public AdminPanelLink(String id, final boolean admin){
		this(id, "", admin);
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
			boolean linkActive = false;
			if (linkActive) return false;
		}
		IContentAdmin session = (IContentAdmin) getSession();
		if (admin) {
			return session.isSuperAdmin();
        } else {
            return session.isLoggedIn();
        }
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		if (StringUtils.isNotEmpty(titleKey)){
			tag.put("title", getString(titleKey));
		}
		super.onComponentTag(tag);
	}

	@Override
	protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
		if (StringUtils.isNotEmpty(key)){
			replaceComponentTagBody(markupStream, openTag, getString(key));
		} else {
			super.onComponentTagBody(markupStream, openTag);
		}
	}

	
}
