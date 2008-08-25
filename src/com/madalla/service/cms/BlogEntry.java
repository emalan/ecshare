package com.madalla.service.cms;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import com.madalla.util.ui.ITreeInput;

public class BlogEntry implements Serializable, Comparable<BlogEntry>, ITreeInput{

	private static final long serialVersionUID = -7829797397130212868L;

	private final static int summaryLength = 200;
    
    private String id; //immutable
    private String blog; //immutable
    private String text;
    private Date date;
    private String category;
    private String title;
    private String description;
    private String keywords;
    
    public static class Builder{
    	//Required parameters
    	private final String id;
    	private final String blog;
    	private final String title;
    	private final Date date;
    	
    	//Optional parameters
    	private String text ="";
    	private String category= "";
    	private String description = "";
    	private String keywords = "";
    	
    	
    	/**
    	 * Constructor for service to create existing Blog entry for use by client
    	 * @param id
    	 * @param blog
    	 * @param title
    	 * @param date
    	 */
    	Builder(String id, String blog, String title, Date date){
    		this.id = id;
    		this.blog = blog;
    		this.title = title;
    		this.date = date;
    	}
    	//creates a new entry that does not yet exist in repository
    	public Builder(String blog, String title, Date date){
    		this.id = "";  //
    		this.blog = blog;
    		this.title = title;
    		this.date = date;
    	}
    	
    	public Builder text(String val){
    		this.text = val; return this;
    	}
    	public Builder category(String val){
    		this.category = val; return this;
    	}
    	public Builder desription(String val){
    		this.description = val; return this;
    	}
    	public Builder keywords(String val){
    		this.keywords = val; return this;
    	}
    	
    	public BlogEntry build() {
    		return new BlogEntry(this);
    	}
    }
    
    /**
     * @param blog - Blog Name
     * @param category - Category
     * @param time - Date Time
     * @param title - 
     */
    private BlogEntry(Builder builder) {
    	this.id = builder.id;
		this.blog = builder.blog;
		this.category = builder.category;
		this.date = builder.date;
		this.title = builder.title;
		this.text = builder.text;
		this.description = builder.description;
		this.keywords = builder.keywords;
	}

	public String getName(){
    	return StringUtils.deleteWhitespace(title);
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
    
    public String getSummary(){
        if (StringUtils.isEmpty(text)){
            return text;
        }
        int textLength = text.length();
        if (textLength <= summaryLength){
            return text;
        }
        
        //Shorten text and add more link
        String firstParagraph = StringUtils.substringBefore(StringUtils.substringBefore(text,"</p>"),"</P>")+"</p>";
        if (summaryLength >= firstParagraph.length()){ //too small
            
        } else { // too big
        	//TODO split on " " and add </p>
        	String[] words = StringUtils.split(firstParagraph,' ');
        }
        return firstParagraph;
    }

    public String getSummary(String moreLink){
        return getSummary()+moreLink;
    }
    
    public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }
    
    @Override
	public boolean equals(Object obj) {
		if (date.equals(obj)) return true;
		if (!(obj instanceof BlogEntry)) return false;
		BlogEntry compare = (BlogEntry)obj; 
		if (id.equals(compare.getId())){
			return true;
		}
		return false;
	}
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	public int compareTo(BlogEntry o) {
		BlogEntry compare = (BlogEntry) o;
		if (date.after(compare.getDate())){
			return -1;
		} else {
			return 1;
		}
	}
	public String getBlog() {
		return blog;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
    public String getId() {
        return id;
    }


}
