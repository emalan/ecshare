package com.madalla.dao.blog;

import java.util.Collection;

import com.madalla.service.blog.BlogEntry;

public interface BlogDao {

    public Collection<BlogEntry> getBlogEntriesForSite();
    public Collection<BlogEntry> getBlogEntriesForCategory(String category);
    public int insertBlogEntry(BlogEntry blogEntry);
    public int saveBlogEntry(BlogEntry blogEntry);
    public void deleteBlogEntry(int blogEntryId);
}
