package com.madalla.cms.bo.impl.ocm.page;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.apache.wicket.markup.html.DynamicWebResource;

import com.madalla.bo.page.IPageData;
import com.madalla.bo.page.ResourceData;

@Node
public class ResourcePdf extends ResourceData {

	private static final long serialVersionUID = 1L;
	private static final Log log = LogFactory.getLog(ResourcePdf.class);

	@Field(path=true) private String id;
	@Field private String urlTitle;
	@Field private String urlDisplay;
	@Field private InputStream inputStream;
	private DynamicWebResource resource;
	
	public ResourcePdf(){
		
	}
	
	public ResourcePdf(final IPageData page, final String name, InputStream inputStream){
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
		this.resource = new DynamicWebResource(){
			private static final long serialVersionUID = 1L;

			@Override
			protected ResourceState getResourceState() {
				return new ResourceState(){

					@Override
					public String getContentType() {
						return ResourceConstants.TYPE_PDF;
					}

					@Override
					public byte[] getData() {
						byte[] rt = new byte[]{};
						try {
							inputStream.read(rt);
						} catch (IOException e) {
							log.error("setInputStream - Exception creating DynamicWebResource",e);
						}
						return rt;
					}
					
				};
			}
			
		};
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public DynamicWebResource getResource() {
		return resource;
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
