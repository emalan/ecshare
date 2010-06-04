package com.madalla.webapp;

import org.apache.wicket.markup.html.basic.Label;

import com.madalla.webapp.cms.ContentPanel;
import com.madalla.webapp.panel.CmsPanel;

public class InfoPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;

	public InfoPanel(String id) {
		super(id);
		
		add(new Label("heading", "Heading one"));
		add(new Label("versionInfo", getBuildInfo().getVersion()));
		add(new ContentPanel("text", "Border"));
	}

}
