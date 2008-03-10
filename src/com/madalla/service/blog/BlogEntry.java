package com.madalla.service.blog;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class BlogEntry implements Serializable{
    private static final long serialVersionUID = 1L;
    public final static String BLOG_ENTRY_ID = "blogEntryId";
    
    private int id;
    private String text;
    private Date date;
    private BlogCategory blogCategory;
    private int siteId;
    
    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}
    
    public BlogCategory getBlogCategory() {
        return blogCategory;
    }
    public void setBlogCategory(BlogCategory blogCategory) {
        this.blogCategory = blogCategory;
    }
    public int getBlogCategoryId(){
        return blogCategory.getId();
    }
    public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }

}
