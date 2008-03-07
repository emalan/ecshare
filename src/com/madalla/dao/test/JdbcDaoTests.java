package com.madalla.dao.test;

import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.madalla.dao.blog.JdbcBlogDao;
import com.madalla.service.blog.BlogCategory;
import com.madalla.service.blog.BlogEntry;
import com.madalla.service.blog.BlogServiceTest;

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
    
        //get Categories
        Collection<BlogCategory> categories = dao.getBlogCategories();
        for (Iterator iter = categories.iterator(); iter.hasNext();) {
			BlogCategory category = (BlogCategory) iter.next();
			log.debug("Category : id="+category.getId()+ " name="+category.getName());
		}
        
        BlogEntry blogEntry = BlogServiceTest.createBlogEntry();
        int id = dao.insertBlogEntry(blogEntry);
        log.debug("Inserted Blog Entry. "+blogEntry);
        
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
        log.debug("Saved Blog Entry. "+blogEntry);
        
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
            log.debug("Deleting Blog Entry. count="+ entries.size());
        	for (Iterator iter = entries.iterator(); iter.hasNext();) {
        		BlogEntry entry = (BlogEntry) iter.next();
        		dao.deleteBlogEntry(entry.getId());
        	}
        }
        
        //insert 3
        {
        	dao.insertBlogEntry(blogEntry);
            log.debug("Inserted Blog Entry. "+blogEntry);
        	dao.insertBlogEntry(blogEntry);
            log.debug("Inserted Blog Entry. "+blogEntry);
        	blogEntry.setCategory(2);
        	dao.insertBlogEntry(blogEntry);
            log.debug("Inserted Blog Entry. "+blogEntry);
        }
        //get all for site
        Collection<BlogEntry> testSelect = dao.getBlogEntriesForSite();
        log.debug("Site blog Entries = "+testSelect.size());
        assertEquals(3, testSelect.size());
        
        //get for category
        Collection<BlogEntry> testCategory1 = dao.getBlogEntriesForCategory(1);
        log.debug("Category 1 blog Entries = "+testCategory1.size());
        assertEquals(2, testCategory1.size());
        Collection<BlogEntry> testCategory2 = dao.getBlogEntriesForCategory(2);
        log.debug("Category 2 blog Entries = "+testCategory2.size());
        assertEquals(1, testCategory2.size());

    }
    

 
}
