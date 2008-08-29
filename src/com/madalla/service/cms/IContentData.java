package com.madalla.service.cms;

/**
 * Implementations should make sure that all returned values are final and immutable
 * 
 * @author exmalan
 *
 */
interface IContentData extends IContentAware {

	String getGroup();
	String getName();
	String getId();
}
