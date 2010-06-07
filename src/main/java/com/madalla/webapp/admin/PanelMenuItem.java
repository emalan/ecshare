package com.madalla.webapp.admin;

import java.io.Serializable;

import org.apache.wicket.markup.html.panel.Panel;

public class PanelMenuItem  implements Serializable {
	private static final long serialVersionUID = 1L;

	public final String key;
	public final String titleKey;
	public final Class<? extends Panel> c;

	public PanelMenuItem(Class<? extends Panel> c, String key, String titleKey){
		this.key = key;
		this.titleKey = titleKey;
		this.c = c;
	}
	
}