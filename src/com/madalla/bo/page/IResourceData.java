package com.madalla.bo.page;

import org.apache.wicket.markup.html.DynamicWebResource;

public interface IResourceData {

	String getId();

	String getName();

	String getValue();
	
	String getTitle();
	
	DynamicWebResource getResource();
}
