package com.madalla.bo.page;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.madalla.cms.bo.impl.ocm.page.CachedWebResource;
import com.madalla.cms.bo.impl.ocm.page.Resource;

public class ResourceHelper {

	private static final Log log = LogFactory.getLog(ResourceHelper.class);
	private static int BUFFER_SIZE = 10*1024;
	
	public enum ResourceType{
		TYPE_PDF("application/pdf", "pdf"), 
		TYPE_DOC("application/msword", "doc"),
		TYPE_ODT("application/vnd.oasis.opendocument.text", "odt");
		
		public final String resourceType;
		public final int bufferSize;
		public final String suffix;
		
		ResourceType(String type, String suffix){
			this.resourceType = type;
			this.suffix = suffix;
			this.bufferSize = BUFFER_SIZE;
		}
		
		public void createWebResource(Resource resource) {
			InputStream inputStream = resource.getInputStream();
			if (null != inputStream) {
				log.debug("createWebResource - creating resource."+resource);
				resource.setResource(new CachedWebResource(inputStream, this, resource.getUrlDisplay()));
			} else {
				log.debug("createWebResource - not able to create web resource." + resource);
			}
		}
	};

	


}
