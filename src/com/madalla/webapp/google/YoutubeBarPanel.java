package com.madalla.webapp.google;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.panel.Panel;

public class YoutubeBarPanel extends Panel{
	
	private static final long serialVersionUID = 1L;
	private static final String GSEARCH_CSS = "http://www.google.com/uds/css/gsearch.css";
	private static final String GSEARCH_SCRIPT = "http://www.google.com/uds/api?file=uds.js&v=1.0";
	private static final String GSVIDEO_CSS = "http://www.google.com/uds/solutions/videobar/gsvideobar.css";
	private static final String GSVIDEO_SCRIPT = "http://www.google.com/uds/solutions/videobar/gsvideobar.js";
	
	
	public YoutubeBarPanel(String id) {
		super(id);
//		add(HeaderContributor.forJavaScript(GSEARCH_SCRIPT));
//		add(HeaderContributor.forCss(GSEARCH_CSS));
//		add(HeaderContributor.forJavaScript(GSVIDEO_SCRIPT));
//		add(HeaderContributor.forCss(GSVIDEO_CSS));
		
	}

}
