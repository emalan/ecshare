package com.madalla.webapp.cms;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class Content implements Serializable {

    private String className;
    private String contentId;
    private String text;
    public void setClassName(String className) {
        this.className = className;
    }
    public String getClassName() {
        return className;
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
