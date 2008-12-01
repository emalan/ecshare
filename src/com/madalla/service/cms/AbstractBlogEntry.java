package com.madalla.service.cms;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.joda.time.DateTime;

import com.madalla.service.cms.jcr.BlogEntry;
import com.madalla.util.ui.HTMLParser;
import com.madalla.util.ui.ICalendarTreeInput;

public abstract class AbstractBlogEntry implements Comparable<AbstractBlogEntry>, ICalendarTreeInput{
	private final static int summaryLength = 500;

	public abstract String save();
    
	public static List<String> getBlogCategories(){
    	List<String> list = new ArrayList<String>();
    	list.add("work");
    	list.add("travel");
        return list;
    }
	
	public String getSummary(String moreLink){
        return getSummary()+moreLink;
    }
	public int compareTo(AbstractBlogEntry o) {
		AbstractBlogEntry compare = o;
		return compare.getDateTime().compareTo(getDateTime());
	}
	
    public String getSummary(){
    	if (StringUtils.isNotEmpty(getDescription())){
    		return getDescription();
    	}
        if (StringUtils.isEmpty(getText()) || getText().length() <= summaryLength){
            return getText();
        }
        return HTMLParser.parseHTMLText(getText(), summaryLength);
    }
    
	public String getName() {
		return StringUtils.deleteWhitespace(getTitle());
	}
	
    @Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof BlogEntry)) return false;
		BlogEntry compare = (BlogEntry)obj; 
		if (!getTitle().equals(compare.getTitle()))return false;
		if (!getBlog().equals(compare.getBlog()))return false;
		if (!getCategory().equals(compare.getCategory()))return false;
		if (!getDescription().equals(compare.getDescription()))return false;
		if (!getKeywords().equals(compare.getKeywords()))return false;
		if (!getDate().toString().equals(compare.getDate().toString()))return false; //dont ask...
		if (!getText().equals(compare.getText()))return false;
		return true;
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

	public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }

	public abstract String getDescription();

	public abstract String getKeywords();

	public abstract String getTitle();

	public abstract DateTime getDate();

	public abstract String getText();

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
	

}