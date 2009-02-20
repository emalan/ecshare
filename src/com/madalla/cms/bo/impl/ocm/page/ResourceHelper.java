package com.madalla.cms.bo.impl.ocm.page;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.madalla.bo.page.ResourceType;


public class ResourceHelper {

	private static final Log log = LogFactory.getLog(ResourceHelper.class);
	
	public static void createWebResource(Resource resource, ResourceType type) {
        InputStream inputStream = resource.getInputStream();
        //LazyFileInputStream fileInputStream = (LazyFileInputStream) inputStream;
        if (null != inputStream) {
            log.debug("createWebResource - creating resource."+resource);
            resource.setResource(new CachedWebResource(inputStream, type, resource.getUrlDisplay()));
        } else {
            log.debug("createWebResource - not able to create web resource." + resource);
        }
    }
		

}
