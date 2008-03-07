package com.madalla.service.blog;

import java.io.Serializable;
import java.util.Date;

public class BlogEntry implements Serializable{
    private static final long serialVersionUID = 1L;
    private int id;
    private String text;
    private Date date;
    private int category;
    private int siteId;
    
    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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
}
