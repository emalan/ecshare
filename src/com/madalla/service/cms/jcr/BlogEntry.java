package com.madalla.service.cms.jcr;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.madalla.service.cms.AbstractBlogEntry;
import com.madalla.service.cms.IRepositoryData;

public class BlogEntry  extends AbstractBlogEntry implements IRepositoryData, Serializable{

	private static final long serialVersionUID = -7829797397130212868L;

    private String id; //immutable
    private String blog; //immutable
    private String text;
    private DateTime date;
    private String category;
    private String title;
    private String description;
    private String keywords;
    
    public static class Builder{
    	//Required parameters
    	private final String id;
    	private final String blog;
    	private final String title;
    	private final DateTime date;
    	
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
    	Builder(final String id, final String blog, final String title, final DateTime date){
    		this.id = id;
    		this.blog = blog;
    		this.title = title;
    		this.date = date;
    	}
    	//creates a new entry that does not yet exist in repository
    	public Builder(final String blog, final String title, final DateTime date){
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
     * @param builder - 
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
    
    public String save(){
    	return BlogEntryHelper.getInstance().save(this);
    }
    
    /* (non-Javadoc)
	 * @see com.madalla.service.cms.jcr.IBlogEntry#getDescription()
	 */
    public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	/* (non-Javadoc)
	 * @see com.madalla.service.cms.jcr.IBlogEntry#getKeywords()
	 */
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	/* (non-Javadoc)
	 * @see com.madalla.service.cms.jcr.IBlogEntry#getTitle()
	 */
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	/* (non-Javadoc)
	 * @see com.madalla.service.cms.jcr.IBlogEntry#getDate()
	 */
	public DateTime getDate() {
        return date;
    }
    public void setDate(DateTime date) {
        this.date = date;
    }
    /* (non-Javadoc)
	 * @see com.madalla.service.cms.jcr.IBlogEntry#getText()
	 */
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    
	/* (non-Javadoc)
	 * @see com.madalla.service.cms.jcr.IBlogEntry#getBlog()
	 */
	public String getBlog() {
		return blog;
	}
	/* (non-Javadoc)
	 * @see com.madalla.service.cms.jcr.IBlogEntry#getCategory()
	 */
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
    /* (non-Javadoc)
	 * @see com.madalla.service.cms.jcr.IBlogEntry#getId()
	 */
    public String getId() {
        return id;
    }
	/* (non-Javadoc)
	 * @see com.madalla.service.cms.jcr.IBlogEntry#getGroup()
	 */
	public String getGroup(){
		return blog;
	}

	/* (non-Javadoc)
	 * @see com.madalla.service.cms.jcr.IBlogEntry#getDateTime()
	 */
	public DateTime getDateTime() {
		return date;
	}


}
