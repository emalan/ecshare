package com.madalla.service.blog;

import java.util.Collection;

public interface IBlogService {
    public abstract Collection<BlogEntry> getBlogEntries(int categoryId);
    public abstract Collection<BlogEntry> getBlogEntries();
    public abstract BlogEntry getBlogEntry(int id);
    public abstract int saveBlogEntry(BlogEntry blogEntry);
}
