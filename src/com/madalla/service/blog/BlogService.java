package com.madalla.service.blog;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import com.madalla.dao.blog.BlogDao;

public class BlogService implements IBlogService, Serializable{
    
	private static final long serialVersionUID = 1L;
	private BlogDao dao;

    public List getBlogCategories(){
        return dao.getBlogCategories();
    }
    public int saveBlogEntry(BlogEntry blogEntry) {
        if (blogEntry.getId() == 0){
            return dao.insertBlogEntry(blogEntry);
        } else {
            return dao.saveBlogEntry(blogEntry);
        }
    }
    
    public BlogEntry getBlogEntry(int id) {
        return dao.getBlogEntry(id);
    }
    
    public List getBlogEntries(int categoryId) {
        return dao.getBlogEntriesForCategory(categoryId);
    }
    
    public List getBlogEntries() {
        return dao.getBlogEntriesForSite();
    }
    
    public void deleteBlogEntry(int id){
        dao.deleteBlogEntry(id);
    }
    
    private BlogEntry createBlogEntry(BlogCategory category, String text){
        BlogEntry entry = new BlogEntry();
        entry.setBlogCategory(category);
        entry.setDate(Calendar.getInstance().getTime());
        entry.setText("First Entry");
        return entry;
    }


    public void setDao(BlogDao dao) {
        this.dao = dao;
    }

        

}
