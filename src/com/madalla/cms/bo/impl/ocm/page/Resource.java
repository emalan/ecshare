package com.madalla.cms.bo.impl.ocm.page;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.apache.wicket.markup.html.DynamicWebResource;

import com.madalla.bo.page.IPageData;
import com.madalla.bo.page.ResourceData;

@Node
public class Resource extends ResourceData {

	private static final long serialVersionUID = 1L;

	@Field(path=true) private String id;
	@Field private String urlTitle;
	@Field private String urlDisplay;
	@Field private transient InputStream inputStream;
	private DynamicWebResource resource;
	
	
	public Resource(){
		
	}
	
	public Resource(final IPageData page, final String name){
		this.setId(page.getId() + "/" + name);
	}
	
	public Resource(final IPageData page, final String name, InputStream inputStream){
		this.setId(page.getId() + "/" + name);
		this.inputStream = inputStream;
	}

	@Override
	public String getName(){
		return StringUtils.substringAfterLast(getId(), "/");
	}

	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInputStream(final InputStream inputStream) {
		this.inputStream = inputStream;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public DynamicWebResource getResource() {
		return resource;
	}
	
	public void setResource(DynamicWebResource dynamicWebResource){
		this.resource = dynamicWebResource;
	}

	public void setUrlDisplay(String urlDisplay) {
		this.urlDisplay = urlDisplay;
	}

	public String getUrlDisplay() {
		return urlDisplay;
	}

	public void setUrlTitle(String urlTitle) {
		this.urlTitle = urlTitle;
	}

	public String getUrlTitle() {
		return urlTitle;
	}

}
