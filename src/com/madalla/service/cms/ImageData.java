package com.madalla.service.cms;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ImageData  implements  IRepositoryData, Serializable {
	private static final long serialVersionUID = -3173685117852794066L;
    private static final Log log = LogFactory.getLog(ImageData.class);

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
	
	public String save(){
		return ImageDataHelper.getInstance().save(this);
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
