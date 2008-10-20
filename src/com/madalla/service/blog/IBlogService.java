package com.madalla.service.blog;

import java.util.List;

import javax.swing.tree.TreeModel;

import com.madalla.service.cms.jcr.BlogEntry;

public interface IBlogService {
	public abstract List<String> getBlogCategories();
	public abstract List<BlogEntry> getBlogEntries(String blog);
    public abstract BlogEntry getBlogEntry(String id);
    public void deleteBlogEntry(String id);
    public TreeModel getBlogEntriesAsTree(String blog);
}
