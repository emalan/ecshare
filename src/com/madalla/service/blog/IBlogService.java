package com.madalla.service.blog;

import java.util.List;

import javax.swing.tree.TreeModel;

public interface IBlogService {
	public abstract List getBlogCategories();
	public List getBlogEntriesForCategory(int categoryId);
	public abstract List getBlogEntries();
    public abstract BlogEntry getBlogEntry(int id);
    public abstract int saveBlogEntry(BlogEntry blogEntry);
    public void deleteBlogEntry(int id);
    public TreeModel getBlogEntriesAsTree();
}
