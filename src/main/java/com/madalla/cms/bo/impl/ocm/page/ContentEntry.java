package com.madalla.cms.bo.impl.ocm.page;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.bo.page.ContentEntryData;
import com.madalla.bo.page.IContentData;

@Node
public class ContentEntry extends ContentEntryData{
	private static final long serialVersionUID = 1L;

	@Field(path=true) private String id;
	@Field private String text;
	
	public ContentEntry(){
		
	}
	
    public ContentEntry(IContentData parent, String name){
        id = parent.getId() + "/" + name;
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
}
