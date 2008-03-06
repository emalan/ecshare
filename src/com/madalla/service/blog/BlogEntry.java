package com.madalla.service.blog;

import java.io.Serializable;
import java.util.Date;

public class BlogEntry implements Serializable{
    private static final long serialVersionUID = 1L;
    private int id;
    private String text;
    private Date date;
    private String category;
    
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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
}
