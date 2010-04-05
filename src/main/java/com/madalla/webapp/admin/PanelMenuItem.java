package com.madalla.webapp.admin;

import java.io.Serializable;

import org.apache.wicket.markup.html.panel.Panel;

public abstract class PanelMenuItem  implements Serializable {
	private static final long serialVersionUID = 1L;

	private String key;
	private String titleKey;

	public PanelMenuItem(String key, String titleKey){
		this.key = key;
		this.titleKey = titleKey;
	}
	
	public String getKey(){
		return key;
	}
	
	public String getTitleKey(){
		return titleKey;
	}
	
	public abstract Panel getPanel(String id);
}