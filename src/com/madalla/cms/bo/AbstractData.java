package com.madalla.cms.bo;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public abstract class AbstractData implements Serializable {

	private static final long serialVersionUID = 1489298293050633840L;
    
	public abstract String getId();

    public abstract String getName();
    
	public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }
}
