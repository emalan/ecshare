package com.madalla.cms.bo.impl.ocm.image;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;

import com.madalla.bo.image.IAlbumData;
import com.madalla.bo.image.ImageData;

@Node
public class Image extends ImageData {

	private static final long serialVersionUID = 1L;

	@Field(path=true) private String id;
	@Field private String title;
	@Field private DynamicImageResource imageFull;
	@Field private DynamicImageResource imageThumb;
	@Field private String url;
	@Field private String urlTitle;
	@Field private String description;
	
	public Image(){
		
	}
	
	public Image(final IAlbumData album, final String name, final InputStream inputStream){
		this.id = album.getId() + "/" + name;
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

	public DynamicImageResource getImageFull() {
        return imageFull;
    }

    public void setImageFull(DynamicImageResource imageFull) {
        this.imageFull = imageFull;
    }

    public DynamicImageResource getImageThumb() {
        return imageThumb;
    }

    public void setImageThumb(DynamicImageResource imageThumb) {
        this.imageThumb = imageThumb;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

}
