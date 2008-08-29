package com.madalla.service.cms;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class ImageData implements IContentData, Serializable {
	private static final long serialVersionUID = -3173685117852794066L;

	private final String name;
	private final String album;
	private final String id;
	
	public ImageData(final String album, final String name){
		id = "";
		this.album = album;
		this.name = name;
	}

	public ImageData(final String album, final String name, String id){
		this.id = id;
		this.album = album;
		this.name = name;
	}

	public String getAlbum() {
		return album;
	}

    public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
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
	
}
