package com.madalla.bo.image;

import org.apache.wicket.markup.html.image.resource.DynamicImageResource;

public interface IImageData {
	String getName();

	String getId();

	DynamicImageResource getImageFull();

	DynamicImageResource getImageThumb();
	
	String getTitle();

	void setTitle(String title);

	String getUrl();

	void setUrl(String url);

	String getUrlTitle();

	void setUrlTitle(String urlTitle);

	String getDescription();

	void setDescription(String description);
	
	Integer getSortOrder();
	
	void setSortOrder(Integer sortOrder);
	
}
