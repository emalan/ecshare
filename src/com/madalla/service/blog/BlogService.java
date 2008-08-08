package com.madalla.service.blog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.madalla.service.cms.IContentService;
import com.madalla.util.ui.CalendarUtils;

public class BlogService implements IBlogService, Serializable{
    
	private static final long serialVersionUID = 1L;
	//TODO refactor to allow multiple blogs per site
	private static final String BLOG_NAME = "mainBlog";
	private IContentService contentService;
	private Log log = LogFactory.getLog(this.getClass());

    public List getBlogCategories(){
    	List list = new ArrayList();
    	list.add("work");
    	list.add("travel");
        return list;
    }

    public void saveBlogEntry(BlogEntry blogEntry) {
        blogEntry.setBlog("mainBlog");
    	contentService.updateBlogEntry(blogEntry);
    }
    
    public BlogEntry getBlogEntry(String id) {
    	BlogEntry blogEntry = contentService.getBlogEntry(id );
        blogEntry.setBlog(BLOG_NAME);
        return blogEntry;
    }
    
    public List getBlogEntries() {
        return contentService.getBlogEntries(BLOG_NAME);
    }

    public TreeModel getBlogEntriesAsTree(){
    	List list = getBlogEntries();
    	return CalendarUtils.createMonthlyTree("Blog Archive", list);
    }
    
    public void deleteBlogEntry(String path){
        contentService.deleteNode(path);
    }
    
	public void setContentService(IContentService contentService) {
		this.contentService = contentService;
	}

        

}
