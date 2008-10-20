package com.madalla.service.cms.jcr;

import junit.framework.TestCase;

import org.joda.time.DateTime;

import com.madalla.service.cms.AbstractBlogEntry;


public class BlogEntryTest extends TestCase{

	public void testBlogEntryContructor(){
		String id = "testID";
		String blog = "testBlog";
		String title = "testTitle";
		String category = "category";
		String description = "description";
		String keywords = "keywords";
		String text = "testText more";
		DateTime date = new DateTime();
		AbstractBlogEntry blogEntry = new BlogEntry.Builder(id, blog, title, date)
			.category(category).desription(description).
			keywords(keywords).text(text).build();

		AbstractBlogEntry sameBlogEntry = new BlogEntry.Builder(id, blog, title, date)
		.category(category).desription(description).
		keywords(keywords).text(text).build();
		
		assertEquals(blogEntry, sameBlogEntry);

		AbstractBlogEntry diffBlogEntry = new BlogEntry.Builder(id, blog, title, date)
		.category(category).desription("different").
		keywords(keywords).text(text).build();
		
		assertNotSame(blogEntry, diffBlogEntry);
		
		

	}
	
}
