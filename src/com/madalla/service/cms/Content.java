package com.madalla.service.cms;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Content implements IContentData, Serializable {
	private static final long serialVersionUID = 1228074714351585867L;
	private final String pageName;
	private String name;
    private final String id;
    private String text;
    
    /**
     * @param pageName
     * @param contentId
     */
    public Content(final String pageName, final String contentName){
    	this.id = "";
    	this.pageName = pageName;
    	this.name = contentName;
    }
    public Content(final String id, final String pageName, final String contentName){
    	this.id = id;
    	this.pageName = pageName;
    	this.name = contentName;
    }
    public String getPageName() {
        return pageName;
    }
    public String getId() {
        return id;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }
	public String getGroup() {
		return pageName;
	}
	public String getName() {
		return name;
	}
    
    
    
}
