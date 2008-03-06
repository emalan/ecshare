package com.madalla.service.blog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.madalla.dao.blog.BlogDao;

public class BlogService implements IBlogService{
    
    private BlogDao dao;

    public List<BlogEntry> getBlogEntries(String category) {
        return getBlogEntries();
    }
    public List<BlogEntry> getBlogEntries() {
        List entries = new ArrayList();
        entries.add(createBlogEntry("travel","First entry"));
        return entries;
    }
    
    private BlogEntry createBlogEntry(String category, String text){
        BlogEntry entry = new BlogEntry();
        entry.setCategory(category);
        entry.setDate(Calendar.getInstance().getTime());
        entry.setText("First Entry");
        return entry;
    }

    public BlogEntry getBlogEntry(int id) {
        // TODO Auto-generated method stub
        return null;
    }
    public int saveBlogEntry(BlogEntry blogEntry) {
        // TODO Auto-generated method stub
        return 0;
    }
    public void setDao(BlogDao dao) {
        this.dao = dao;
    }

        

}
