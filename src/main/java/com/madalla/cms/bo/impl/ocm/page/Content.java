package com.madalla.cms.bo.impl.ocm.page;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.IPageData;

@Node
public class Content extends ContentData {
    private static final long serialVersionUID = 1L;

    @Field(path=true) private String id;
    @Field private String text;

    public Content(){

    }
    public Content(IPageData page, String name){
        id = page.getId() + "/" + name;
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
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
}
