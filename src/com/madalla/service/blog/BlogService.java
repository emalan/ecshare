package com.madalla.service.blog;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.madalla.dao.blog.BlogDao;
import com.madalla.service.cms.IContentService;
import com.madalla.webapp.cms.Content;

public class BlogService implements IBlogService, Serializable{
    
	private static final long serialVersionUID = 1L;
	private static final String BLOG_PREFIX = "blog";
	private BlogDao dao;
	private IContentService contentService;
	private Log log = LogFactory.getLog(this.getClass());

    public List getBlogCategories(){
        return dao.getBlogCategories();
    }

    public int saveBlogEntry(BlogEntry blogEntry) {
    	int blogId;
        if (blogEntry.getId() == 0){
        	log.debug("saveBlogEntry - inserting "+blogEntry);
        	blogId = dao.insertBlogEntry(blogEntry);
        } else {
        	log.debug("saveBlogEntry - updating "+blogEntry);
        	dao.saveBlogEntry(blogEntry);
        	blogId = blogEntry.getId();
        }
        //save content to CMS
        Content content = new Content();
        content.setClassName(dao.getSite());
        content.setContentId(getBlogId(blogId));
        content.setText(blogEntry.getText());
        try {
        	log.debug("saveBlogEntry - saving Content "+content);
			contentService.setContent(content);
		} catch (RepositoryException e) {
			log.error("saveBlogEntry - ", e);
		}
        
        return blogId;
    }
    
    public BlogEntry getBlogEntry(int id) {
        BlogEntry blogEntry = dao.getBlogEntry(id);
        populateContentFromCMS(blogEntry);
        return blogEntry;
    }
    
    public List getBlogEntriesForCategory(int categoryId) {
        List list = dao.getBlogEntriesForCategory(categoryId);
        return getBlogEntries(list);
    }
    
    public List getBlogEntries() {
    	//get Metadata from database
        List list = dao.getBlogEntriesForSite();
        return getBlogEntries(list);
    }

    public TreeModel getBlogEntriesAsTree(){
    	List list = getBlogEntries();
    	return createBlogArchive(list);
    }
    
    public void deleteBlogEntry(int id){
        dao.deleteBlogEntry(id);
    }

    private TreeModel createBlogArchive(List list) {
        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Blog Archive");
        TreeModel model = new DefaultTreeModel(rootNode);
        
        DateFormat df = new SimpleDateFormat("MMMMM yyyy");
        DateFormat blogFormat = DateFormat.getDateInstance();
        
        
        //Get Blogs in tree of months
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        
        for (Iterator iter = list.iterator(); iter.hasNext();) {
			BlogEntry blogEntry = (BlogEntry) iter.next();
			Date date = blogEntry.getDate();
			while (calendar.getTime().after(date)){
				calendar.add(Calendar.MONTH, -1);
			}
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(df.format(calendar.getTime()));
			rootNode.add(node);
			node.add(new DefaultMutableTreeNode(blogFormat.format(blogEntry.getDate())+" Blog Entry Title goes here"));
			
		}
        
        return model;
    }
    
    private void populateContentFromCMS(BlogEntry blogEntry){
    	log.debug("populateContent - before Content :"+blogEntry);
    	String node = dao.getSite();
		String id = getBlogId(blogEntry.getId());
		
		log.debug("populateContent - retrieve content from CMS for node="+node+" id="+id);
		String content = contentService.getContentData(node, id);
		log.debug("populateContent - content="+content);
		blogEntry.setText(content);
    }
    
    private String getBlogId(int id){
    	return BLOG_PREFIX + Integer.toString(id);
    }

    /**
     * Content from CMS repository and sorts it
     * @param list
     * @return
     */
    private List getBlogEntries(List list){
        for (Iterator iter = list.iterator(); iter.hasNext();) {
			BlogEntry blogEntry = (BlogEntry) iter.next();
			populateContentFromCMS(blogEntry);
		}
        Collections.sort(list);
        return list;
    }

    public void setDao(BlogDao dao) {
        this.dao = dao;
    }
	public void setContentService(IContentService contentService) {
		this.contentService = contentService;
	}

        

}
