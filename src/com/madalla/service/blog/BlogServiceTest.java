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
    BlogDao mockDao;
    IContentService mockContentService;
    private MockControl daoControl;
    private MockControl contentControl;
    
    protected void setUp() {
        daoControl = MockControl.createControl(BlogDao.class);
        mockDao = (BlogDao) daoControl.getMock();
        
        contentControl = MockControl.createControl(IContentService.class);
        mockContentService = (IContentService) contentControl.getMock();
        
        service = new BlogService();
        service.setDao(mockDao);
        service.setContentService(mockContentService);
    }
    
    public void testInsert(){
        BlogEntry blogEntry = createBlogEntry();
        mockDao.insertBlogEntry(blogEntry);
        daoControl.setReturnValue(1);
        daoControl.replay();

        int id = service.saveBlogEntry(blogEntry);
        daoControl.verify();
        
    }
    
    public void testSave(){
        BlogEntry blogEntry = createBlogEntry();
        blogEntry.setId(1);
        mockDao.saveBlogEntry(blogEntry);
        daoControl.setReturnValue(1);
        daoControl.replay();

        int id = service.saveBlogEntry(blogEntry);
        daoControl.verify(); 
    }
    
    public void testGetBlogEntry(){
        mockDao.getBlogEntry(1);
        daoControl.setReturnValue(createBlogEntry());
        daoControl.replay();

        BlogEntry blogEntry = service.getBlogEntry(1);
        daoControl.verify();
    }
    
    public void testGetAllBlogEntries(){
        mockDao.getBlogEntriesForSite();
        Collection test = new ArrayList();
        BlogEntry entry = createBlogEntry();
        test.add(entry);
        daoControl.setReturnValue(test);


        daoControl.replay();
        
        Collection entries = service.getBlogEntries();
        daoControl.verify();
    }
    
    public void testGetAllBlogEntriesForCategory(){
        mockDao.getBlogEntriesForCategory(1);
        Collection test = new ArrayList();
        test.add(createBlogEntry());
        daoControl.setReturnValue(test);

        
        daoControl.replay();
        Collection entries = service.getBlogEntriesForCategory(1);
        daoControl.verify();
    }
    
    public static BlogEntry createBlogEntry(){
        BlogEntry blogEntry = new BlogEntry();
        blogEntry.setBlogCategory(new BlogCategory(1,"test"));
        blogEntry.setDate(Calendar.getInstance().getTime());
        blogEntry.setText("Test entry");
        blogEntry.setTitle("test title");
        blogEntry.setDescription("test description");
        blogEntry.setKeywords("test keywords");
        return blogEntry;
    }
    
}
