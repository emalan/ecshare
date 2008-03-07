package com.madalla.dao.test;

import java.util.Calendar;

import javax.sql.DataSource;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.madalla.dao.blog.JdbcBlogDao;
import com.madalla.service.blog.BlogEntry;

public class JdbcDaoTests extends  AbstractDependencyInjectionSpringContextTests{
 
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
        BlogEntry blogEntry = new BlogEntry();
        blogEntry.setCategory(1);
        blogEntry.setDate(Calendar.getInstance().getTime());
        blogEntry.setText("Test entry");
        blogEntry.setSiteId(1);
        int id = dao.insertBlogEntry(blogEntry);
        
        BlogEntry testInsert = dao.getBlogEntry(id);
        assertNotNull(testInsert);
        assertEquals(blogEntry.getText(), testInsert.getText());
        
        blogEntry.setId(id);
        blogEntry.setText("Test Save Entry");
        dao.saveBlogEntry(blogEntry);
        
        
    }
    
    public void testBlogCategories(){
    	
    }
}
