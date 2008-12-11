package com.madalla.service.cms;

import org.joda.time.DateTime;

public interface IBlogEntryData {

	String getId();

	String getName();

	String getDescription();

	String getKeywords();

	String getTitle();

	DateTime getDate();

	String getText();

	String getBlog();

	String getCategory();

	DateTime getDateTime();

	void setTitle(String title);

	void setDate(DateTime dateTime);

	void setKeywords(String keywords);

	void setText(String text);

	void setDescription(String description);

	void setCategory(String category);

}
