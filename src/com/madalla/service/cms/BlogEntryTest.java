package com.madalla.service.cms;

import junit.framework.TestCase;

import org.joda.time.DateTime;

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
		BlogEntry blogEntry = new BlogEntry.Builder(id, blog, title, date)
			.category(category).desription(description).
			keywords(keywords).text(text).build();

		BlogEntry sameBlogEntry = new BlogEntry.Builder(id, blog, title, date)
		.category(category).desription(description).
		keywords(keywords).text(text).build();
		
		assertEquals(blogEntry, sameBlogEntry);

		BlogEntry diffBlogEntry = new BlogEntry.Builder(id, blog, title, date)
		.category(category).desription("different").
		keywords(keywords).text(text).build();
		
		assertNotSame(blogEntry, diffBlogEntry);
		
		

	}
	
}
