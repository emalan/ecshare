package com.madalla.webapp.admin;

import java.io.Serializable;

import org.apache.wicket.markup.html.panel.Panel;

public interface IPanelMenuItem extends Serializable {

	String getKey();
	String getTitleKey();
	Panel getPanel(String id);
	
}
