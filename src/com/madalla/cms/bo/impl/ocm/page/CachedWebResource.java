package com.madalla.cms.bo.impl.ocm.page;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.wicket.markup.html.DynamicWebResource;

import com.madalla.bo.page.ResourceHelper.ResourceType;

public class CachedWebResource extends DynamicWebResource {
	private static final long serialVersionUID = 1L;

	private byte[] cache ;
	private String contentType;
	
	/**
	 * 
	 */
	public CachedWebResource(InputStream inputStream, ResourceType type) {
		super("test.pdf");
		this.contentType = type.resourceType;
		try {
			cache = CachedWebResource.bytes(inputStream, type.bufferSize);
		} catch (IOException e) {
			cache = null;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.apache.wicket.markup.html.DynamicWebResource#getResourceState()
	 */
	@Override
	protected ResourceState getResourceState() {
		return new ResourceState() {
			
			@Override
			public String getContentType() {
				return contentType;
			}
			
			@Override
			public byte[] getData() {
				return cache;
			}
		};
	}
	
	public static  byte[] bytes(InputStream is, int buffersize) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copy(is, out, buffersize);
		return out.toByteArray();
	}
	
	public static void copy(InputStream is, OutputStream os, int bufferSize) throws IOException {
		byte[] buf = new byte[bufferSize];
		while (true) {
			int tam = is.read(buf);
			if (tam == -1) {
				return;
			}
			os.write(buf, 0, tam);
		}
	}



}
