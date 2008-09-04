package com.madalla.service.blog;

import java.util.ArrayList;
import java.util.Collection;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.joda.time.DateTime;

import com.madalla.service.cms.BlogEntry;
import com.madalla.service.cms.IRepositoryService;

public class BlogServiceTest extends TestCase{

    BlogService service ;
    IRepositoryService mockContentService;
    private MockControl contentControl;
    private static String BLOG = "testBlog";
    
    protected void setUp() {
        contentControl = MockControl.createControl(IRepositoryService.class);
        mockContentService = (IRepositoryService) contentControl.getMock();
        
        service = new BlogService();
        service.setRepositoryService(mockContentService);
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
        
        Collection<BlogEntry> entries = service.getBlogEntries(BLOG);
    }
    
    public void testGetAllBlogEntriesForCategory(){
        Collection<BlogEntry> test = new ArrayList<BlogEntry>();
        test.add(createBlogEntry());
    }
    
    public static BlogEntry createBlogEntry(){
    	BlogEntry blogEntry = new BlogEntry.Builder(BLOG, "test title", 
    			new DateTime()).category("travel").text("Test entry")
    			.desription("test Description").keywords("test keywords").build();
        return blogEntry;
    }
    
}
