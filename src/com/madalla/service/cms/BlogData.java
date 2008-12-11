package com.madalla.service.cms;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public abstract class BlogData extends AbstractData implements IBlogData {

	private static final long serialVersionUID = -8295687070730684709L;

	public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }
}
