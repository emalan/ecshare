package com.madalla.webapp.admin.pages;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Application;
import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.InstantiationPermissions;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class AdminPanelLink extends Link<Object> {

	private static final long serialVersionUID = 1L;
	protected static final String ID = "adminPanel";
	private Logger log = LoggerFactory.getLogger(this.getClass());

	final private String key;
	final private String titleKey;
	final private Class<? extends Panel> panelClass;

	public AdminPanelLink(final String id, Class<? extends Panel> panelClass, final String key, String titleKey){
		super(id);
		this.key = key;
		this.titleKey = titleKey;
		this.panelClass = panelClass;
		setAuthorization();
	}

	public AdminPanelLink(final String id, Class<? extends Panel> panelClass, final String key){
		this(id, panelClass, key, "");
	}

    public AdminPanelLink(String id, Class<? extends Panel> panelClass) {
		this(id, panelClass, "");
	}

    private void setAuthorization(){
    	final Application application = Application.get();
		InstantiationPermissions permissions = application.getMetaData(MetaDataRoleAuthorizationStrategy.INSTANTIATION_PERMISSIONS);
		if (permissions != null){
	    	Roles roles = permissions.authorizedRoles(panelClass);
	    	if (roles != null){
	    		for (Iterator<String> iter = roles.iterator(); iter.hasNext();){
	    			MetaDataRoleAuthorizationStrategy.authorize(this, ENABLE, iter.next());
	    		}
	    	}
		}
    }

	@Override
	public boolean isEnabled() {
		Component currentPanel = getPage().get(ID);
		if (currentPanel != null){
			if (currentPanel.getClass().equals(panelClass) ){
				log.debug(currentPanel.toString());
				return false;
			}
			return true;
		} else {
			return true;
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
	public void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
		if (StringUtils.isNotEmpty(key)){
			replaceComponentTagBody(markupStream, openTag, getString(key));
		} else {
			super.onComponentTagBody(markupStream, openTag);
		}
	}

	@Override
	public void onClick() {
		try {
			Constructor<? extends Panel> constructor = panelClass.getConstructor(String.class);
			Panel panel = constructor.newInstance(new Object[]{ID});
			getPage().replace(panel);
		} catch (InvocationTargetException e){
			log.error("Invocation Exception while creating admin panel. If this was caused by Authorized Exception, then take a look at why link was enabled???", e);
			throw new WicketRuntimeException("Error while Creating new Admin Panel.", e);
		} catch (Exception e) {
			log.error("Error while creating admin panel.", e);
			throw new WicketRuntimeException("Error while Creating new Admin Panel.", e);
		}

	}


}
