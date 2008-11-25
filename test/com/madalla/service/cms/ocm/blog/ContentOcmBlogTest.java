package com.madalla.service.cms.ocm.blog;

import com.madalla.service.cms.ocm.AbstractContentOcmTest;

public class ContentOcmBlogTest extends AbstractContentOcmTest {

	///ec:apps/ec:test/ec:blogs/ec:testBlog
	public void testOcmBlogInsert(){
		//Fetch or create Blogs node
		
		
		
		Blog blog = new Blog();
		blog.setPath("/myFolder");
		blog.setTitle("MyNewOCMBlog");
		ocm.insert(blog);
	}
}
