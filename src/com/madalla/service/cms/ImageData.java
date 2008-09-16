package com.madalla.service.cms;

import java.io.InputStream;
import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class ImageData  implements  IRepositoryData, Serializable, Comparable<ImageData> {
	private static final long serialVersionUID = -3173685117852794066L;

	private final String name;
	private final String album;
	private final String id;
	private String title;
	private String description;
	private final InputStream fullImage;


	public ImageData(final String album, final String name, final InputStream fullImage){
		this("", album, name, fullImage);
	}

	public ImageData(final String id, final String album, final String name, final InputStream fullImage){
		this.id = id;
		this.album = album;
		this.name = name;
		this.fullImage = fullImage;
		title = "";
		description = "";
	}
	
	public String save(){
		return ImageDataHelper.getInstance().save(this);
	}
	
	public String getAlbum() {
		return album;
	}

	public String getGroup() {
		return album;
	}

	public String getName() {
		return name;
	}

	public String getId(){
		return id;
	}

	public InputStream getFullImage() {
		return fullImage;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	//TODO implement this, cause we need to support sorting images in album
	public int compareTo(ImageData compare) {
		return compare.getName().compareTo(getName());
	}
	
    @Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof ImageData)) return false;
		ImageData compare = (ImageData)obj; 
		if (!id.equals(compare.getId())) return false;
		if (!album.equals(compare.getAlbum())) return false;
		if (!name.equals(compare.getName())) return false;
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
