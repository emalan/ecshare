package com.madalla.cms.bo.impl.ocm.page;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.bo.page.IPageData;
import com.madalla.bo.page.PageMetaData;

@Node
public class PageMeta  extends PageMetaData {
	private static final long serialVersionUID = 1L;
	
	//There is only one per Page so name is hardcoded
	public static final String NAME = "MetaInfo";
	
    @Field(path=true) private String id;
    
    public PageMeta(){
    	
    }
    public PageMeta(IPageData parent){
    	setId(parent.getId() + "/" + NAME);
    }
    public String getName() {
    	return StringUtils.substringAfterLast(getId(), "/");
    }
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
}
