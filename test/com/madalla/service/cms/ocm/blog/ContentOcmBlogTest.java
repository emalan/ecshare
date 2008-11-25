package com.madalla.service.cms.ocm.blog;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;

import com.madalla.service.cms.ocm.AbstractContentOcmTest;
import com.madalla.util.jcr.JcrUtils;

public class ContentOcmBlogTest extends AbstractContentOcmTest {

	
	private static final String NS_BLOG = NS+"blogs";
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public void testOcmBlogInsert() throws RepositoryException{

		
		//getCreate test Node
		// /ec:apps/ec:test/ec:blogs/ec:testBlog
		String testPath = getCreateBlogsNode();
		
		//Create Blog
		{
			Blog blog = new Blog();
			blog.setId(testPath+"/ec:MyOcmBlog");
			String title = "testOcmblog";
			blog.setTitle(title);
			//blog.setKeywords(keywords);
			//blog.setDescription(description);
			ocm.insert(blog);
			ocm.save();
			Blog testResult = (Blog) ocm.getObject(Blog.class, blog.getId());
			assertNotNull(testResult);
			assertEquals(blog.getTitle(), title);
		}
		//get Blog, edit and save
		
		//create Blog Entry
		
		//edit Blog Entry
		
		//get Blog and Entries
		
		//edit Blog - make sure entries are still there
		
		
	}
	
	protected String getCreateBlogsNode(){
		return (String) template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				Node test = getTestNode(session);
				Node blogs = JcrUtils.getCreateNode(NS_BLOG, test);
				
				//create random folder
				String random = RandomStringUtils.randomAlphabetic(5);
				Node node = JcrUtils.getCreateNode(random, blogs);
				session.save();
				log.debug("created Node with path: "+node.getPath());
				return node.getPath();
			}
			
		});
	}

}
