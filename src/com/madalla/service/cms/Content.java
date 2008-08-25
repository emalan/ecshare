package com.madalla.service.cms;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Content implements Serializable {
	private static final long serialVersionUID = 1228074714351585867L;
	private final String pageName;
    private final String contentId;
    private String text;
    
    /**
     * @param pageName
     * @param contentId
     */
    public Content(final String pageName, final String contentId){
    	this.pageName = pageName;
    	this.contentId = contentId;
    }
    public String getPageName() {
        return pageName;
    }
    public String getContentId() {
        return contentId;
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
    
    
    
}
