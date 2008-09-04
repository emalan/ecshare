package com.madalla.service.blog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.tree.TreeModel;

import com.madalla.service.cms.BlogEntry;
import com.madalla.service.cms.IRepositoryService;
import com.madalla.util.ui.CalendarUtils;

public class BlogService implements IBlogService, Serializable{
    
	private static final long serialVersionUID = 1L;
	private IRepositoryService repositoryService;
	
    public List<String> getBlogCategories(){
    	List<String> list = new ArrayList<String>();
    	list.add("work");
    	list.add("travel");
        return list;
    }

    public BlogEntry getBlogEntry(String id) {
    	BlogEntry blogEntry = repositoryService.getBlogEntry(id);
        return blogEntry;
    }
    
    public List<BlogEntry> getBlogEntries(String blog) {
    	List<BlogEntry> list = repositoryService.getBlogEntries(blog);
    	if (list != null){
    		Collections.sort(list);
    	}
        return list;
    }

    public TreeModel getBlogEntriesAsTree(String blog){
    	List<BlogEntry> list = getBlogEntries(blog);
    	return CalendarUtils.createMonthlyTree("Blog Archive", list);
    }
    
    public void deleteBlogEntry(String path){
        repositoryService.deleteNode(path);
    }
    
	public void setRepositoryService(IRepositoryService repositoryService) {
		this.repositoryService = repositoryService;
	}

        

}
