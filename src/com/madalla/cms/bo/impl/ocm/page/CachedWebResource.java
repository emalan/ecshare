package com.madalla.cms.bo.impl.ocm.page;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.html.DynamicWebResource;

import com.madalla.bo.page.ResourceType;

public class CachedWebResource extends DynamicWebResource {
	private static final long serialVersionUID = 1L;
	private static final int BUFFER_SIZE = 10*1024;
	private byte[] cache ;
	private String contentType;
	
	/**
	 * 
	 */
	public CachedWebResource(InputStream inputStream, ResourceType type, String file) {
		super(validatedFileName(file, type.suffix));
		this.contentType = type.resourceType;
		try {
			cache = CachedWebResource.bytes(inputStream);
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
	
	public static  byte[] bytes(InputStream is) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		copy(is, out);
		return out.toByteArray();
	}
	
	public static void copy(InputStream is, OutputStream os) throws IOException {
		byte[] buf = new byte[BUFFER_SIZE];
		while (true) {
			int tam = is.read(buf);
			if (tam == -1) {
				return;
			}
			os.write(buf, 0, tam);
		}
	}
	
	private static String validatedFileName(String file, String suffix){
		if (StringUtils.isEmpty(file)){
			return "temp."+suffix;
		} else if (file.endsWith(suffix)){
			return file;
		} else {
			return file+"."+suffix;
		}
	}



}
