package com.madalla.webapp;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.madalla.webapp.cms.InlineContentPanel;

public class InfoPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;

	public InfoPanel(final String id) {
		super(id);

		add(new InlineContentPanel("heading"));
		add(new Label("versionShare", new Model<String>(getBuildInfo().getVersion())));
		add(new Label("versionApp", new Model<String>(getBuildInfo().getWebappVersion())));
		
	}

}
