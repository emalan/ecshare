package com.madalla.service.cms.ocm.page;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.service.cms.ocm.AbstractOcm;

@Node
public class Page extends AbstractOcm implements Serializable{
    private static final long serialVersionUID = 1L;

    @Field(path=true) private String id;
    
    public Page(){
        
    }
    public Page(String parentPath, String name){
        id = parentPath + "/" + name;
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
    
	public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }
    
}
