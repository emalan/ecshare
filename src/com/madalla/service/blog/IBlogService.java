package com.madalla.service.blog;

import java.util.List;

import javax.swing.tree.TreeModel;

import org.joda.time.DateTime;

import com.madalla.service.cms.AbstractBlogEntry;

public interface IBlogService {
	public abstract List<String> getBlogCategories();
	public abstract List<AbstractBlogEntry> getBlogEntries(String blog);
    public abstract AbstractBlogEntry getBlogEntry(String id);
    public abstract AbstractBlogEntry getNewBlogEntry(String blog, String title, DateTime date);
    public void deleteBlogEntry(String id);
    public TreeModel getBlogEntriesAsTree(String blog);
}
