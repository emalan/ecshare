package com.madalla.webapp.google;

import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.webapp.scripts.JavascriptResources;

public class YoutubePlayerPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public YoutubePlayerPanel(String id) {
		super(id);
		add(JavascriptResources.SWF_OBJECT);
	}

}
