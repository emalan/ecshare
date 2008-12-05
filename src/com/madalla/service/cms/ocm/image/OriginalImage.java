package com.madalla.service.cms.ocm.image;

import java.io.InputStream;

import com.madalla.service.cms.jcr.ImageData;

public class OriginalImage extends Image {

	private static final long serialVersionUID = 1L;

	public OriginalImage(final String name, final InputStream image){
		
	}
	public String saveOriginalImage(final String name, final InputStream fullImage ){
        //ImageData imageData = new ImageData(EC_ORIGINALS, name, fullImage);
        //String path = save(imageData);
        //createThumbnail(path);
        //return path;
		return "";
    }
}
