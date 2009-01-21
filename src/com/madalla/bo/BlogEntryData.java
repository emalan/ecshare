package com.madalla.bo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.madalla.util.ui.HTMLParser;
import com.madalla.util.ui.ICalendarTreeInput;

public abstract class BlogEntryData extends AbstractData implements IBlogEntryData,Comparable<BlogEntryData>, ICalendarTreeInput {

	private static final long serialVersionUID = -4384559542235332563L;
	private static final int summaryLength = 500;

	public static List<String> getBlogCategories(){
    	List<String> list = new ArrayList<String>();
    	list.add("work");
    	list.add("travel");
        return list;
    }
	
	public String getSummary(String moreLink){
        return getSummary()+moreLink;
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
	
	public int compareTo(BlogEntryData o) {
		BlogEntryData compare = o;
		return compare.getDateTime().compareTo(getDateTime());
	}	
    
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(obj instanceof BlogEntryData)) return false;
		BlogEntryData compare = (BlogEntryData)obj; 
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
}
