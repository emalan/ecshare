package com.madalla.webapp;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.madalla.webapp.cms.ContentPanel;
import com.madalla.webapp.cms.InlineContentPanel;
import com.madalla.webapp.panel.CmsPanel;

public class InfoPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;

	public InfoPanel(String id) {
		super(id);
		
		add(new InlineContentPanel("heading"));
		add(new Label("versionInfo", new Model<String>(getString("label.buildinfo") +": " + getBuildInfo().getVersion())));
		add(new ContentPanel("text"));
	}

}
