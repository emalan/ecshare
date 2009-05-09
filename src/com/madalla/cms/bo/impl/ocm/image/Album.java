package com.madalla.cms.bo.impl.ocm.image;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.bo.image.AlbumData;

@Node
public class Album extends AlbumData implements Serializable{

	private static final long serialVersionUID = 1L;

	@Field(path=true) private String id;
	@Field private String title;
	@Field private String type;
	@Field private Integer width;
	@Field private Integer height;
	@Field private Integer interval;
	
	public Album(){
		
	}
	
	public Album(String parentPath, String name){
		id = parentPath + "/" + name;
	}
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public Integer getHeight() {
        return height;
    }

    public Integer getWidth() {
        return width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }
	
}
