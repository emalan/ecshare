package com.madalla.service.blog;

import java.util.List;

public interface IBlogService {
	public abstract List<BlogCategory> getBlogCategories();
    public abstract List<BlogEntry> getBlogEntries(int categoryId);
    public abstract List<BlogEntry> getBlogEntries();
    public abstract BlogEntry getBlogEntry(int id);
    public abstract int saveBlogEntry(BlogEntry blogEntry);
    public void deleteBlogEntry(int id);
}
