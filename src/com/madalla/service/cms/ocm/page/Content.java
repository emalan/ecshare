package com.madalla.service.cms.ocm.page;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.service.cms.AbstractData;

@Node
public class Content extends AbstractData implements Serializable{
    private static final long serialVersionUID = 1L;

    @Field(path=true) private String id;
    @Field private String text;
    
    public Content(){
        
    }
    public Content(Page page, String name){
        id = page.getId() + "/" + name;
    }
    public String getName(){
        return StringUtils.substringAfterLast(getId(), "/");
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    
	public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }
    
}
