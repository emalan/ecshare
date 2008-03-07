package com.madalla.dao.blog;

import java.util.List;

import com.madalla.service.blog.BlogCategory;
import com.madalla.service.blog.BlogEntry;

public interface BlogDao {

    public List<BlogCategory> getBlogCategories();
    public List<BlogEntry> getBlogEntriesForSite();
    public List<BlogEntry> getBlogEntriesForCategory(int category);
    public BlogEntry getBlogEntry(int blogEntryId);
    public int insertBlogEntry(BlogEntry blogEntry);
    public int saveBlogEntry(BlogEntry blogEntry);
    public void deleteBlogEntry(int blogEntryId);
}
