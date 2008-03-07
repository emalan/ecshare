package com.madalla.dao.test;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.madalla.dao.blog.JdbcBlogDao;
import com.madalla.service.blog.BlogCategory;
import com.madalla.service.blog.BlogEntry;

public class JdbcDaoTests extends  AbstractDependencyInjectionSpringContextTests{
	
	Log log = LogFactory.getLog(this.getClass());
 
    protected void onSetUp() throws Exception {
    	DataSource dataSource = (DataSource) getApplicationContext().getBean("dataSource");
        TestDatabaseUtils.setupDatabase(dataSource);
    }

    protected String[] getConfigLocations() {
        
        return new String[]{
        		"classpath:com/madalla/service/blog/applicationContext-blog.xml",
                "classpath:com/madalla/dao/test/applicationContext-test.xml"};
    }

    public void testBlogInsertSaveDelete(){
        JdbcBlogDao dao = (JdbcBlogDao) getApplicationContext().getBean("blogDao");
        dao.setSite("test");
    
        //get Categories
        Collection<BlogCategory> categories = dao.getBlogCategories();
        for (Iterator iter = categories.iterator(); iter.hasNext();) {
			BlogCategory category = (BlogCategory) iter.next();
			log.debug("Category : id="+category.getId()+ " name="+category.getName());
		}
        
        BlogEntry blogEntry = new BlogEntry();
        blogEntry.setCategory(1);
        blogEntry.setDate(Calendar.getInstance().getTime());
        blogEntry.setText("Test entry");
        blogEntry.setSiteId(1);
        int id = dao.insertBlogEntry(blogEntry);
        
        //test insert
        BlogEntry testInsert = dao.getBlogEntry(id);
        assertNotNull(testInsert);
        assertEquals(blogEntry.getText(), testInsert.getText());
        
        //test update
        blogEntry.setId(id);
        blogEntry.setText("Test Save Entry");
        dao.saveBlogEntry(blogEntry);
        BlogEntry testUpdate = dao.getBlogEntry(id);
        assertNotNull(testUpdate);
        assertEquals(testUpdate.getText(), blogEntry.getText());
        
        //test delete
        dao.deleteBlogEntry(id);
        try {
            dao.getBlogEntry(id);
            fail("Expected EmptyResultDataAccessException.");
        } catch (EmptyResultDataAccessException e){
        	//expected
        }
        
        //make sure there are none
        {
        	Collection<BlogEntry> entries = dao.getBlogEntriesForSite();
        	for (Iterator iter = entries.iterator(); iter.hasNext();) {
        		BlogEntry entry = (BlogEntry) iter.next();
        		dao.deleteBlogEntry(entry.getId());
        	}
        }
        
        //insert 3
        {
        	dao.insertBlogEntry(blogEntry);
        	dao.insertBlogEntry(blogEntry);
        	blogEntry.setCategory(2);
        	dao.insertBlogEntry(blogEntry);
        }
        //get all for site
        Collection<BlogEntry> testSelect = dao.getBlogEntriesForSite();
        assertEquals(3, testSelect.size());
        
        //get for category
        Collection<BlogEntry> testCategory1 = dao.getBlogEntriesForCategory(1);
        assertEquals(2, testCategory1.size());
        Collection<BlogEntry> testCategory2 = dao.getBlogEntriesForCategory(2);
        assertEquals(1, testCategory2.size());

    }
 
}
