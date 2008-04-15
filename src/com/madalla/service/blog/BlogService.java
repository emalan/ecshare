package com.madalla.service.blog;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import com.madalla.dao.blog.BlogDao;
import com.madalla.service.cms.IContentService;

public class BlogService implements IBlogService, Serializable{
    
	private static final long serialVersionUID = 1L;
	private BlogDao dao;
	private IContentService contentService;

    public List getBlogCategories(){
        return dao.getBlogCategories();
    }
    public int saveBlogEntry(BlogEntry blogEntry) {
        if (blogEntry.getId() == 0){
            return dao.insertBlogEntry(blogEntry);
        } else {
            return dao.saveBlogEntry(blogEntry);
        }
    }
    
    public BlogEntry getBlogEntry(int id) {
        return dao.getBlogEntry(id);
    }
    
    //TODO Sort Blogs latest date first
    public List getBlogEntries(int categoryId) {
        return dao.getBlogEntriesForCategory(categoryId);
    }
    
    //TODO Sort Blogs latest date first
    public List getBlogEntries() {
    	//get Metadata from database
        List list = dao.getBlogEntriesForSite();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
			BlogEntry blogEntry = (BlogEntry) iter.next();
			String node = dao.getSiteId().toString();
			String id = Integer.toString(blogEntry.getId());
			String content = contentService.getContentData(node, id);
			blogEntry.setText(content);
		}
        return list;
    }
    
    public void deleteBlogEntry(int id){
        dao.deleteBlogEntry(id);
    }
    
    private BlogEntry createBlogEntry(BlogCategory category, String text){
        BlogEntry entry = new BlogEntry();
        entry.setBlogCategory(category);
        entry.setDate(Calendar.getInstance().getTime());
        entry.setText("First Entry");
        return entry;
    }


    public void setDao(BlogDao dao) {
        this.dao = dao;
    }
	public void setContentService(IContentService contentService) {
		this.contentService = contentService;
	}

        

}
