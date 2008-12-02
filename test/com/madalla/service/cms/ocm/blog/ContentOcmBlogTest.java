package com.madalla.service.cms.ocm.blog;

import java.io.IOException;
import java.util.Collection;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.ocm.query.Filter;
import org.apache.jackrabbit.ocm.query.Query;
import org.apache.jackrabbit.ocm.query.QueryManager;
import org.joda.time.DateTime;
import org.springmodules.jcr.JcrCallback;

import com.madalla.service.cms.ocm.AbstractContentOcmTest;
import com.madalla.util.jcr.JcrUtils;

public class ContentOcmBlogTest extends AbstractContentOcmTest {

	
	private static final String NS_BLOG = NS+"blogs";
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public void testOcmBlogEntry() throws RepositoryException{

		
		//getCreate test Node
		// /ec:apps/ec:test/ec:blogs/ec:testBlog
		String blogsPath = getCreateBlogsNode();
		
		String blogName = RandomStringUtils.randomAlphabetic(5)+"Blog";
		String blogPath ;
		{
			//Create Blog
			Blog blog = new Blog(blogsPath, blogName);
			blogPath = blog.getId();
			String title = "Test Ocm Blog";
			blog.setTitle(title);
			blog.setKeywords("test, keyword");
			blog.setDescription("test description");
			log.debug("inserting blog :"+blog.getId());
			ocm.insert(blog);
			ocm.save();
			Blog testResult = (Blog) ocm.getObject(Blog.class, blog.getId());
			assertNotNull(testResult);
			assertEquals(blog.getTitle(), title);
		}
		{
			//get Blog, edit and save
			Blog blog = (Blog) ocm.getObject(Blog.class, blogPath);
			String newDescription = "other description";
			blog.setDescription(newDescription);
			ocm.update(blog);
			ocm.save();
			Blog testResult = (Blog) ocm.getObject(Blog.class, blogPath);
			assertEquals(newDescription, testResult.getDescription());
		}
		
		String blogEntryPath;
		{
			//create Blog Entry
			Blog blog = (Blog) ocm.getObject(Blog.class, blogPath);

			String title = "First Blog Entry";
			BlogEntry blogEntry = new BlogEntry(blog, title, new DateTime());
			String category = "test Category";
			blogEntry.setCategory(category);
			DateTime dateTime = new DateTime();
			blogEntry.setDate(dateTime);
			ocm.insert(blogEntry);
			ocm.save();
			
			BlogEntry testEntry = (BlogEntry) ocm.getObject(BlogEntry.class, blogEntry.getId());
			assertNotNull(testEntry);
			assertEquals(category, testEntry.getCategory());
			assertEquals(dateTime, testEntry.getDate());
		}
		
		{
			//get Blog , edit, then get Entries
			Blog blog = (Blog) ocm.getObject(Blog.class, blogPath);
			blog.setTitle("edited");
			ocm.update(blog);
			ocm.save();
			
			QueryManager queryManager = ocm.getQueryManager();
			Filter filter = queryManager.createFilter(BlogEntry.class);
			filter.setScope(blog.getId()+"//");
			Query query = queryManager.createQuery(filter);
			Collection blogEntries = ocm.getObjects(query);
			log.info("retrieved blog entries :"+blogEntries.size());
			assertTrue(blogEntries.size()>0);
		}
		
		ocm.remove(blogPath);
		ocm.save();
		
	}
	
	protected String getCreateBlogsNode(){
		return (String) template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node test = getTestNode(session);
				Node blogs = JcrUtils.getCreateNode(NS_BLOG, test);
				session.save();
				log.debug("created Node with path: "+blogs.getPath());
				return blogs.getPath();
			}
			
		});
	}

}
