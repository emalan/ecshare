package com.madalla.bo.page;

import java.io.InputStream;

public interface IResourceData {

	String getId();

	String getName();
	
	String getUrlDisplay();
	
	void setUrlDisplay(String urlDisplay);

	String getUrlTitle();

	void setUrlTitle(String urlTitle) ;
	
	InputStream getInputStream();
	
	void setInputStream(InputStream inputStream);
	
	String getType();
	
	void setType(String type);
	
	boolean isHideLink();
	
	void setHideLink(boolean hide);
}
