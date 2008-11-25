package com.madalla.service.cms.ocm.blog;

import java.io.Serializable;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.joda.time.DateTime;

import com.madalla.service.cms.AbstractBlogEntry;

@Node
public class BlogEntry extends AbstractBlogEntry implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Field(path=true) private final String id;
    @Field private final String blog; //immutable
    private DateTime date;
    @Field private String title;
    @Field private String text;
    @Field private String category;
    @Field private String description;
    @Field private String keywords;
	
    public BlogEntry(final String id, final String blog, final String title, final DateTime date) {
		this.id = id;
		this.blog = blog;
		this.title = title;
		this.date = date;
    }
    
    public BlogEntry(final String blog, final String title, final DateTime date){
		this.id = "";
		this.blog = blog;
		this.title = title;
		this.date = date;
    }
    
    @Override
    public String getId() {
		return id;
	}
	
	@Override
	public String getBlog() {
		return blog;
	}

	@Override
	public String getTitle() {
		return title;
	}
	@Override
	public DateTime getDate() {
		return date;
	}

	@Override
	public DateTime getDateTime() {
		return date;
	}

	@Override
	public String getCategory() {
		return category;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getKeywords() {
		return keywords;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public String getGroup() {
		// TODO get rid of this
		return null;
	}

	@Override
	public String save() {
		// TODO Remove 
		return null;
	}

	@Override
	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public void setDate(DateTime dateTime) {
		this.date = dateTime;
	}

	@Override
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public void setTitle(String title) {
		this.title = title;
	}
}
