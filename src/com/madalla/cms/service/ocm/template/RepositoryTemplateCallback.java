package com.madalla.cms.service.ocm.template;

import com.madalla.bo.AbstractData;



/**
 * Callback used by Template for doing type specific tasks.
 * 
 * @author Eugene Malan
 *
 */
public abstract class RepositoryTemplateCallback {
	public abstract AbstractData createNew(String parentPath, String name);
}
