package com.madalla.bo.page;

import org.apache.wicket.markup.html.DynamicWebResource;

public interface IResourceData {

	String getId();

	String getName();
	
	DynamicWebResource getResource();
	
	void setUrlDisplay(String urlDisplay);

	public String getUrlDisplay();

	public void setUrlTitle(String urlTitle) ;

	public String getUrlTitle();
}
