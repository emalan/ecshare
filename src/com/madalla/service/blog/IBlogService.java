package com.madalla.service.blog;

import java.util.List;

import javax.swing.tree.TreeModel;

public interface IBlogService {
	public abstract List getBlogCategories();
	public abstract List getBlogEntries();
    public abstract BlogEntry getBlogEntry(int id);
    public abstract void saveBlogEntry(BlogEntry blogEntry);
    public void deleteBlogEntry(int id);
    public TreeModel getBlogEntriesAsTree();
}
