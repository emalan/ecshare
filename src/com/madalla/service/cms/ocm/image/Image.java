package com.madalla.service.cms.ocm.image;

import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;

import com.madalla.service.cms.jcr.ImageData;

@Node
public class Image implements Serializable, Comparable<Image>{

	private static final long serialVersionUID = 1L;

	@Field(path=true) private String id;
	@Field private String title;
	private InputStream imageFull;
	private InputStream imageThumb;
	@Field private String url;
	@Field private String urlTitle;
	@Field private String description;
	
	private DynamicImageResource webResource;
	private DynamicImageResource thumbnail;
	
	public String getName(){
		return StringUtils.substringAfterLast(getId(), "/");
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public InputStream getImageFull() {
		return imageFull;
	}

	public void setImageFull(InputStream imageFull) {
		this.imageFull = imageFull;
	}

	public InputStream getImageThumb() {
		return imageThumb;
	}

	public void setImageThumb(InputStream imageThumb) {
		this.imageThumb = imageThumb;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlTitle() {
		return urlTitle;
	}

	public void setUrlTitle(String urlTitle) {
		this.urlTitle = urlTitle;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	//TODO implement this, cause we need to support sorting images in album
	public int compareTo(Image compare) {
		return compare.getName().compareTo(getName());
	}
	
    @Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof ImageData)) return false;
		ImageData compare = (ImageData)obj; 
		if (!id.equals(compare.getId())) return false;
		if (!title.equals(compare.getTitle())) return false;
		if (!description.equals(compare.getDescription()))return false;
		return true;
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }

}
