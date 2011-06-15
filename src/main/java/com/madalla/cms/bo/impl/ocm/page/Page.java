package com.madalla.cms.bo.impl.ocm.page;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.bo.page.PageData;

@Node
public class Page extends PageData {
    private static final long serialVersionUID = 1L;

    @Field(path=true) private String id;

    public Page(){

    }
    public Page(String parentPath, String name){
        id = parentPath + "/" + name;
    }
    @Override
	public String getName(){
        return StringUtils.substringAfterLast(getId(), "/");
    }
    @Override
	public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

	@Override
	public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }

}
