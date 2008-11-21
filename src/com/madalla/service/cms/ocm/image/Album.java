package com.madalla.service.cms.ocm.image;

import java.util.Map;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Collection;

public class Album {

	@Collection private Map<String, Image> images;
}
