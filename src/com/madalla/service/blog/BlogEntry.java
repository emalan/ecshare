package com.madalla.service.blog;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class BlogEntry implements Serializable, Comparable{
	private static final long serialVersionUID = 1L;
    private final static int summaryLength = 2000;
    
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
    public String getSummary(){
        if (StringUtils.isEmpty(text)){
            return text;
        }
        int textLength = text.length();
        if (textLength <= summaryLength){
            return text;
        }
        
        String para1 = StringUtils.substringBefore(StringUtils.substringBefore(text,"</p>"),"</P>");
        if (summaryLength >= para1.length()){ //too big
            //TODO split on " " and add </p>
        } else { // to small
            //TODO get next paragraph as well
        }
        return para1;
    }
    public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }
	public boolean equals(Object obj) {
		if (date.equals(obj)) return true;
		if (!(obj instanceof BlogEntry)) return false;
		BlogEntry compare = (BlogEntry)obj; 
		if (date.equals(compare.getDate())){
			if (id == compare.id){
				return true;
			}
		}
		return false;
	}
	public int compareTo(Object o) {
		BlogEntry compare = (BlogEntry) o;
		if (date.after(compare.getDate())){
			return -1;
		} else {
			return 1;
		}
	}

}
