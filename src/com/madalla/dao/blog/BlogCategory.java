package com.madalla.dao.blog;

import java.io.Serializable;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public class BlogCategory implements Serializable{
	
	private static final long serialVersionUID = 6591017566642399532L;
	private int id;
    private String name;
    public BlogCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }
}
