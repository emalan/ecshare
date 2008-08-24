package com.madalla.service.blog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.TreeModel;

import com.madalla.service.cms.IContentService;
import com.madalla.util.ui.CalendarUtils;

public class BlogService implements IBlogService, Serializable{
    
	private static final long serialVersionUID = 1L;
	private IContentService contentService;
	
    public List<String> getBlogCategories(){
    	List<String> list = new ArrayList<String>();
    	list.add("work");
    	list.add("travel");
        return list;
    }

    public void saveBlogEntry(BlogEntry blogEntry) {
    	contentService.updateBlogEntry(blogEntry);
    }
    
    public BlogEntry getBlogEntry(String id) {
    	BlogEntry blogEntry = contentService.getBlogEntry(id);
        return blogEntry;
    }
    
    public List<BlogEntry> getBlogEntries(String blog) {
        return contentService.getBlogEntries(blog);
    }

    public TreeModel getBlogEntriesAsTree(String blog){
    	List list = getBlogEntries(blog);
    	return CalendarUtils.createMonthlyTree("Blog Archive", list);
    }
    
    public void deleteBlogEntry(String path){
        contentService.deleteNode(path);
    }
    
	public void setContentService(IContentService contentService) {
		this.contentService = contentService;
	}

        

}
