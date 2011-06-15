package com.madalla.bo.image;

import com.madalla.bo.AbstractData;
import com.madalla.cms.bo.impl.ocm.image.Image;

public abstract class ImageData extends AbstractData implements IImageData, Comparable<ImageData> {

	private static final long serialVersionUID = -176900396141783576L;

	private String albumName;

	public int compareTo(ImageData compare) {
		// TODO implement this, cause we need to support sorting images in album
		return compare.getName().compareTo(getName());
	}

    @Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof Image)) return false;
		Image compare = (Image)obj;
		if (!getId().equals(compare.getId())) return false;
		if (!getTitle().equals(compare.getTitle())) return false;
		if (!getDescription().equals(compare.getDescription()))return false;
		return true;
	}

    public String getResourceReference(){
    	return albumName + ".full." + getName();
    }

    public String getMountUrl(){
    	return "resource/"+albumName+"/full/" + getName();
    }

	public void setAlbumName(String albumName) {
		this.albumName = albumName;
	}

	public String getAlbumName() {
		return albumName;
	}


}
