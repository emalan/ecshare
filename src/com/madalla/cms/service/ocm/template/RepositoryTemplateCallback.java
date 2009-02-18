package com.madalla.cms.service.ocm.template;

import com.madalla.bo.AbstractData;



public abstract class RepositoryTemplateCallback {
	public abstract AbstractData createNew(String parentPath, String name);
}
