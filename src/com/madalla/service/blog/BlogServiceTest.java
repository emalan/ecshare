package com.madalla.service.blog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import junit.framework.TestCase;

import org.easymock.MockControl;

import com.madalla.dao.blog.BlogDao;
import com.madalla.service.cms.IContentService;

public class BlogServiceTest extends TestCase{

    BlogService service ;
    IContentService mockContentService;
    private MockControl contentControl;
    private static String BLOG = "testBlog";
    
    protected void setUp() {
        contentControl = MockControl.createControl(IContentService.class);
        mockContentService = (IContentService) contentControl.getMock();
        
        service = new BlogService();
        service.setContentService(mockContentService);
    }
    
    public void testInsert(){
        BlogEntry blogEntry = createBlogEntry();

        service.saveBlogEntry(blogEntry);
        
    }
    
    public void testSave(){
        BlogEntry blogEntry = createBlogEntry();

        service.saveBlogEntry(blogEntry);
    }
    
    public void testGetBlogEntry(){
        BlogEntry blogEntry = service.getBlogEntry("1");
    }
    
    public void testGetAllBlogEntries(){
        Collection test = new ArrayList();
        BlogEntry entry = createBlogEntry();
        test.add(entry);
        
        Collection entries = service.getBlogEntries(BLOG);
    }
    
    public void testGetAllBlogEntriesForCategory(){
        Collection test = new ArrayList();
        test.add(createBlogEntry());
    }
    
    public static BlogEntry createBlogEntry(){
        BlogEntry blogEntry = new BlogEntry(BLOG, "travel",Calendar.getInstance().getTime(),"test title");
        blogEntry.setText("Test entry");
        blogEntry.setDescription("test description");
        blogEntry.setKeywords("test keywords");
        return blogEntry;
    }
    
}
