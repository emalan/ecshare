package com.madalla.service.blog;

import java.util.List;

public interface IBlogService {
	public abstract List getBlogCategories();
    public abstract List getBlogEntries(int categoryId);
    public abstract List getBlogEntries();
    public abstract BlogEntry getBlogEntry(int id);
    public abstract int saveBlogEntry(BlogEntry blogEntry);
    public void deleteBlogEntry(int id);
}
