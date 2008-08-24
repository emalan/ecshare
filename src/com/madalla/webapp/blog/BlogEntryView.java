package com.madalla.webapp.blog;

import java.io.Serializable;
import java.util.Date;

import com.madalla.service.blog.BlogEntry;

public class BlogEntryView implements Serializable{

	private static final long serialVersionUID = -6278544701328724786L;
	
	private String id; 
    private String blog; 
	private String text;
    private Date date;
    private String category;
    private String title;
    private String description;
    private String keywords;
    
    public void init(BlogEntry newData){
    	setId(newData.getId());
		setTitle(newData.getTitle());
		setDate(newData.getDate());
		setKeywords(newData.getKeywords());
		setText(newData.getText());
		setDescription(newData.getDescription());
		setCategory(newData.getCategory());
    }
    public void populate(BlogEntry old){
    	old.setTitle(title);
    	old.setDate(date);
    	old.setKeywords(keywords);
    	old.setText(text);
    	old.setDescription(description);
    	old.setCategory(category);
    }
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBlog() {
		return blog;
	}
	public void setBlog(String blog) {
		this.blog = blog;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
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

}
