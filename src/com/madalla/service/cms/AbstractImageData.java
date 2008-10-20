package com.madalla.service.cms;

import java.io.InputStream;

import org.apache.wicket.Resource;

public abstract class AbstractImageData {

	public abstract InputStream getFullImageAsInputStream();
	public abstract Resource getFullImageAsResource();
	public abstract String getUrl();
	public abstract void setTitle(String title);
}
