package com.madalla.cms.bo.impl.ocm.page;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

import com.madalla.bo.page.IPageMetaData;
import com.madalla.bo.page.PageMetaLangData;

@Node
public class PageMetaLang extends PageMetaLangData {
	private static final long serialVersionUID = 1L;
	
	@Field(path=true) private String id;
	@Field private String title;
	@Field private String lang;
	@Field private String description;
	@Field private String keywords;
	@Field private String author;
    
    public PageMetaLang(){
    	
    }
    public PageMetaLang(IPageMetaData parent, String name){
    	setId(parent.getId() + "/" + name);
    }
    /* (non-Javadoc)
	 * @see com.madalla.cms.bo.impl.ocm.page.IPageMetaLangData#getName()
	 */
    public String getName() {
    	return StringUtils.substringAfterLast(getId(), "/");
    }
	/* (non-Javadoc)
	 * @see com.madalla.cms.bo.impl.ocm.page.IPageMetaLangData#setId(java.lang.String)
	 */
	public void setId(String id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see com.madalla.cms.bo.impl.ocm.page.IPageMetaLangData#getId()
	 */
	public String getId() {
		return id;
	}
	/* (non-Javadoc)
	 * @see com.madalla.cms.bo.impl.ocm.page.IPageMetaLangData#getTitle()
	 */
	public String getTitle() {
		return title;
	}
	/* (non-Javadoc)
	 * @see com.madalla.cms.bo.impl.ocm.page.IPageMetaLangData#setTitle(java.lang.String)
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/* (non-Javadoc)
	 * @see com.madalla.cms.bo.impl.ocm.page.IPageMetaLangData#getLang()
	 */
	public String getLang() {
		return lang;
	}
	/* (non-Javadoc)
	 * @see com.madalla.cms.bo.impl.ocm.page.IPageMetaLangData#setLang(java.lang.String)
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}
	/* (non-Javadoc)
	 * @see com.madalla.cms.bo.impl.ocm.page.IPageMetaLangData#getDescription()
	 */
	public String getDescription() {
		return description;
	}
	/* (non-Javadoc)
	 * @see com.madalla.cms.bo.impl.ocm.page.IPageMetaLangData#setDescription(java.lang.String)
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/* (non-Javadoc)
	 * @see com.madalla.cms.bo.impl.ocm.page.IPageMetaLangData#getKeywords()
	 */
	public String getKeywords() {
		return keywords;
	}
	/* (non-Javadoc)
	 * @see com.madalla.cms.bo.impl.ocm.page.IPageMetaLangData#setKeywords(java.lang.String)
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	/* (non-Javadoc)
	 * @see com.madalla.cms.bo.impl.ocm.page.IPageMetaLangData#getAuthor()
	 */
	public String getAuthor() {
		return author;
	}
	/* (non-Javadoc)
	 * @see com.madalla.cms.bo.impl.ocm.page.IPageMetaLangData#setAuthor(java.lang.String)
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

}
