package com.madalla.service.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractContentData implements IContentData {

	private static final Log log = LogFactory.getLog(AbstractContentData.class);
	static final String NS = "ec:";
    
    //TODO remove this Node to make data structure more consitent
    static final String EC_NODE_CONTENT = NS + "content";
    
    //shared repository properties
	static final String EC_PROP_CONTENT = NS + "content";



    


	
}
