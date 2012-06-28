package com.madalla.webapp.panelmenu;

import java.io.Serializable;

import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

/**
 * Has all the info needed to instantiate the relevant panel
 * for that menu item.
 * 
 * @author Eugene Malan
 *
 */
public class PanelMenuItem implements Serializable{
	private static final long serialVersionUID = 1L;

	public final IModel<String> key;
	public final IModel<String> titleKey;
	public final Class<? extends Panel> c;
	public final Object constructorArg;

	/**
	 * @param c - Panel that menu should link to, will replace existing panel
	 * @param key - Model for displaying menu item.
	 * @param titleKey - Model for value on link title - the hover display - not required
	 */
	public PanelMenuItem(final Class<? extends Panel> c, final IModel<String> key, final IModel<String> titleKey){
		this(c,null, key, titleKey);
	}

	private PanelMenuItem(final Class<? extends Panel> c, final Object constructorArg, final IModel<String> key,
			final IModel<String> titleKey) {
		this.key = key;
		this.titleKey = titleKey;
		this.c = c;
		this.constructorArg = constructorArg;
	}
	
}
