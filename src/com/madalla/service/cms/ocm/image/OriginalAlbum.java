package com.madalla.service.cms.ocm.image;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;


@Node
public class OriginalAlbum extends Album{

	private static final long serialVersionUID = 1L;
	public static final String ORIGINAL_ALBUM_NAME = "originals";
	
	public OriginalAlbum(String parentPath){
		super(parentPath, ORIGINAL_ALBUM_NAME);
	}

}
