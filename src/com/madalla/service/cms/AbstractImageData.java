package com.madalla.service.cms;

import java.io.InputStream;

import org.apache.wicket.Resource;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;

public abstract class AbstractImageData implements IRepositoryData{

	public abstract InputStream getFullImageAsInputStream();
	public abstract Resource getFullImageAsResource();
	public abstract String getUrl();
	public abstract void setTitle(String title);
	public abstract DynamicImageResource getThumbnail();
	public abstract String createOriginalImage(final String imageName, final InputStream fullImage);
}
