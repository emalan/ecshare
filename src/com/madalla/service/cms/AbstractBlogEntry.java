package com.madalla.service.cms;

import org.joda.time.DateTime;

import com.madalla.util.ui.ICalendarTreeInput;

public abstract class AbstractBlogEntry implements Comparable<AbstractBlogEntry>, ICalendarTreeInput{

	public abstract String getName();

	public abstract String getDescription();

	public abstract String getKeywords();

	public abstract String getTitle();

	public abstract DateTime getDate();

	public abstract String getText();

	public abstract String getSummary();

	public abstract String getSummary(String moreLink);

	public abstract String getBlog();

	public abstract String getCategory();

	public abstract String getId();

	public abstract String getGroup();

	public abstract DateTime getDateTime();
	
	public abstract void setTitle(String title);
	
	public abstract void setDate(DateTime dateTime);

	public abstract void setKeywords(String keywords);

	public abstract void setText(String text);

	public abstract void setDescription(String description);

	public abstract void setCategory(String category);
	
	public abstract String save();

	public int compareTo(AbstractBlogEntry o) {
		AbstractBlogEntry compare = (AbstractBlogEntry) o;
		return compare.getDateTime().compareTo(getDateTime());
	}


}