package com.madalla.service.blog;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.madalla.util.ui.ITreeInput;

public class BlogEntry implements Serializable, Comparable, ITreeInput{
	private static final long serialVersionUID = 1L;
    private final static int summaryLength = 200;
    
    private String id;
    private String blog;
    private String text;
    private Date date;
    private String blogCategory;
    private String title;
    private String description;
    private String keywords;
    
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
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

    public String getSummary(String moreLink){
        if (StringUtils.isEmpty(text)){
            return text;
        }
        int textLength = text.length();
        if (textLength <= summaryLength){
            return text;
        }
        
        //Shorten text and add more link
        String firstParagraph = StringUtils.substringBefore(StringUtils.substringBefore(text,"</p>"),"</P>")+"</p>";
        if (summaryLength >= firstParagraph.length()){ //too small
            
        } else { // too big
        	//TODO split on " " and add </p>
        	String[] words = StringUtils.split(firstParagraph,' ');
        }
        return firstParagraph+moreLink;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }
    
	public boolean equals(Object obj) {
		if (date.equals(obj)) return true;
		if (!(obj instanceof BlogEntry)) return false;
		BlogEntry compare = (BlogEntry)obj; 
		if (date.equals(compare.getDate())){
			return true;
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
	public String getBlog() {
		return blog;
	}
	public void setBlog(String blog) {
		this.blog = blog;
	}
	public String getBlogCategory() {
		return blogCategory;
	}
	public void setBlogCategory(String blogCategory) {
		this.blogCategory = blogCategory;
	}
    public String getId() {
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

}
