package com.madalla.cms.bo.impl.ocm.page;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.wicket.markup.html.DynamicWebResource;

import com.madalla.bo.page.IPageData;
import com.madalla.bo.page.ResourceData;

public class Resource extends ResourceData {

	private static final long serialVersionUID = 1L;

	@Field(path=true) private String id;
	@Field private String title;
	@Field private String value;
	@Field private DynamicWebResource resource;
	
	public Resource(){
		
	}
	
	public Resource(final IPageData page, final String name){
		this.id = page.getId() + "/" + name;
	}

	@Override
	public String getName(){
		return StringUtils.substringAfterLast(getId(), "/");
	}

	@Override
	public String getId() {
		return id;
	}

	public DynamicWebResource getResource() {
		return resource;
	}

	public String getTitle() {
		return title;
	}

	public String getValue() {
		return value;
	}

}
