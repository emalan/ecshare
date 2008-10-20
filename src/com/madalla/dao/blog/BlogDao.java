package com.madalla.dao.blog;

import java.util.List;

import com.madalla.service.blog.BlogCategory;
import com.madalla.service.cms.jcr.BlogEntry;

public interface BlogDao {

    public List getBlogCategories();
    public BlogCategory getBlogCategory(int category);
    public List getBlogEntriesForSite();
    public List getBlogEntriesForCategory(int category);
    public BlogEntry getBlogEntry(int blogEntryId);
    public int insertBlogEntry(BlogEntry blogEntry);
    public int saveBlogEntry(BlogEntry blogEntry);
    public void deleteBlogEntry(int blogEntryId);
    public Integer getSiteId();
}
