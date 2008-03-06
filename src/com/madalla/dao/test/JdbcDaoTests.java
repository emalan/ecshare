package com.madalla.dao.test;

import java.util.Calendar;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.madalla.dao.blog.JdbcBlogDao;
import com.madalla.service.blog.BlogEntry;

public class JdbcDaoTests extends  AbstractDependencyInjectionSpringContextTests{
 
    protected void onSetUp() throws Exception {
        TestDatabaseUtils.createDatabase();
    }

    protected String[] getConfigLocations() {
        
        return new String[]{"classpath:com/madalla/service/blog/applicationContext-blog.xml",
                "classpath:com/madalla/dao/test/applicationContext-test.xml"};
    }

    public void testBlogInsert(){
        JdbcBlogDao dao = (JdbcBlogDao) getApplicationContext().getBean("blogDao");
        BlogEntry blogEntry = new BlogEntry();
        blogEntry.setCategory(1);
        blogEntry.setDate(Calendar.getInstance().getTime());
        blogEntry.setText("Test entry");
        dao.insertBlogEntry(blogEntry);
    }
}
