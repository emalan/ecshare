package com.madalla.dao.blog;

import java.util.List;

import com.madalla.service.blog.BlogCategory;
import com.madalla.service.cms.AbstractBlogEntry;

public interface BlogDao {

    public List getBlogCategories();
    public BlogCategory getBlogCategory(int category);
    public List getBlogEntriesForSite();
    public List getBlogEntriesForCategory(int category);
    public AbstractBlogEntry getBlogEntry(int blogEntryId);
    public int insertBlogEntry(AbstractBlogEntry blogEntry);
    public int saveBlogEntry(AbstractBlogEntry blogEntry);
    public void deleteBlogEntry(int blogEntryId);
    public Integer getSiteId();
}
