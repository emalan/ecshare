package com.madalla.cms.bo.impl.ocm.blog;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.joda.time.DateTime;

import com.madalla.bo.blog.BlogEntryData;
import com.madalla.bo.blog.IBlogData;

@Node
public class BlogEntry extends BlogEntryData {

	private static final long serialVersionUID = 1L;

	@Field(path = true)	private String id;
	private String blog;
	@Field	private DateTime date;
	@Field	private String title;
	@Field	private String text;
	@Field	private String category;
	@Field	private String description;
	@Field	private String keywords;

	public BlogEntry() {

	}

	/**
	 * @param blog
	 * @param title
	 * @param date
	 */
	public BlogEntry(IBlogData blog, final String title, final DateTime date) {
		this.title = title;
		this.date = date;
		this.id = blog.getId() + "/" + getName();
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

	public String getTitle() {
		return title;
	}

	public DateTime getDate() {
		return date;
	}

	public DateTime getDateTime() {
		return date;
	}

	public String getCategory() {
		return category;
	}

	public String getDescription() {
		return description;
	}

	public String getKeywords() {
		return keywords;
	}

	public String getText() {
		return text;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setDate(DateTime dateTime) {
		this.date = dateTime;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
