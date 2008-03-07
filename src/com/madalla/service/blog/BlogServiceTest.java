package com.madalla.service.blog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import junit.framework.TestCase;

import org.easymock.MockControl;

import com.madalla.dao.blog.BlogDao;

public class BlogServiceTest extends TestCase{

    BlogService service ;
    BlogDao mockDao;
    private MockControl control;
    
    protected void setUp() {
        control = MockControl.createControl(BlogDao.class);
        mockDao = (BlogDao) control.getMock();
        service = new BlogService();
        service.setDao(mockDao);
    }
    
    public void testInsert(){
        BlogEntry blogEntry = createBlogEntry();
        mockDao.insertBlogEntry(blogEntry);
        control.setReturnValue(1);
        control.replay();
        int id = service.saveBlogEntry(blogEntry);
        control.verify();
        
    }
    
    public void testSave(){
        BlogEntry blogEntry = createBlogEntry();
        blogEntry.setId(1);
        mockDao.saveBlogEntry(blogEntry);
        control.setReturnValue(1);
        control.replay();
        int id = service.saveBlogEntry(blogEntry);
        control.verify(); 
    }
    
    public void testGetBlogEntry(){
        mockDao.getBlogEntry(1);
        control.setReturnValue(createBlogEntry());
        control.replay();
        BlogEntry blogEntry = service.getBlogEntry(1);
        control.verify();
    }
    
    public void testGetAllBlogEntries(){
        mockDao.getBlogEntriesForSite();
        Collection<BlogEntry> test = new ArrayList();
        test.add(createBlogEntry());
        control.setReturnValue(test);
        control.replay();
        Collection<BlogEntry> entries = service.getBlogEntries();
        control.verify();
    }
    
    public void testGetAllBlogEntriesForCategory(){
        mockDao.getBlogEntriesForCategory(1);
        Collection<BlogEntry> test = new ArrayList();
        test.add(createBlogEntry());
        control.setReturnValue(test);
        control.replay();
        Collection<BlogEntry> entries = service.getBlogEntries(1);
        control.verify();
    }
    
    public static BlogEntry createBlogEntry(){
        BlogEntry blogEntry = new BlogEntry();
        blogEntry.setCategory(1);
        blogEntry.setDate(Calendar.getInstance().getTime());
        blogEntry.setText("Test entry");
        return blogEntry;
    }
    
}
