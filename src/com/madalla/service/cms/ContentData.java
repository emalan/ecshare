package com.madalla.service.cms;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public abstract class ContentData extends AbstractData implements IContentData {

	private static final long serialVersionUID = 6454017439319972085L;

	public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }
}
