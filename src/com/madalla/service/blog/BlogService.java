package com.madalla.service.blog;

import java.util.Calendar;
import java.util.Collection;

import com.madalla.dao.blog.BlogDao;

public class BlogService implements IBlogService{
    
    private BlogDao dao;

    public Collection<BlogCategory> getBlogCategories(){
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
    
    public Collection<BlogEntry> getBlogEntries(int categoryId) {
        return dao.getBlogEntriesForCategory(categoryId);
    }
    
    public Collection<BlogEntry> getBlogEntries() {
        return dao.getBlogEntriesForSite();
    }
    
    private BlogEntry createBlogEntry(int category, String text){
        BlogEntry entry = new BlogEntry();
        entry.setCategory(category);
        entry.setDate(Calendar.getInstance().getTime());
        entry.setText("First Entry");
        return entry;
    }


    public void setDao(BlogDao dao) {
        this.dao = dao;
    }

        

}
