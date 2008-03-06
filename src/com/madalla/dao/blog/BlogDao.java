package com.madalla.dao.blog;

import java.util.Collection;

import com.madalla.service.blog.BlogEntry;

public interface BlogDao {

    public Collection<BlogEntry> getBlogEntriesForSite();
    public Collection<BlogEntry> getBlogentriesForCategory(String category);
}
