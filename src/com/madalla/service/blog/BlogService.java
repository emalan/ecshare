package com.madalla.service.blog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BlogService implements IBlogService{
    private String site;

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

    public void setSite(String site) {
        this.site = site;
    }

        

}
