package com.madalla.bo.page;

import java.io.InputStream;

import org.apache.wicket.markup.html.WebResource;

public interface IResourceData {

	String getId();

	String getName();
	
	WebResource getResource();
	
	void setUrlDisplay(String urlDisplay);

	String getUrlDisplay();

	void setUrlTitle(String urlTitle) ;
	
	String getUrlTitle();
	
	void setInputStream(InputStream inputStream);
	
}
