package com.madalla.service.cms.ocm.template;

import com.madalla.service.cms.AbstractData;



public abstract class ParentNodeCallback {
	public abstract AbstractData createNew(String parentPath, String name);
}
