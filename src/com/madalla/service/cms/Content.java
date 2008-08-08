package com.madalla.service.cms;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Content implements Serializable {
    private static final long serialVersionUID = 1L;
    private String pageName;
    private String contentId;
    private String text;
    public void setPageName(String className) {
        this.pageName = className;
    }
    public String getPageName() {
        return pageName;
    }
    public void setContentId(String contentId) {
        this.contentId = contentId;
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
