package com.madalla.cms.bo.impl.ocm.page;

import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ResourceHelper {

	private static final Log log = LogFactory.getLog(ResourceHelper.class);

	public static void createWebResource(ResourcePdf resource) {
		InputStream inputStream = resource.getInputStream();
		if (null != inputStream) {
			log.debug("createWebResource - creating pdf resource."+resource);
			resource.setResource(new PdfResource(inputStream));
		} else {
			log.debug("createWebResource - not able to create web resource." + resource);
		}
	}
}
