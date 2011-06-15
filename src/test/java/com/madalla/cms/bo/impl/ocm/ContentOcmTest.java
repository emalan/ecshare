package com.madalla.cms.bo.impl.ocm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.jcr.RepositoryException;

import org.apache.jackrabbit.ocm.query.Filter;
import org.apache.jackrabbit.ocm.query.Query;
import org.apache.jackrabbit.ocm.query.QueryManager;
import org.joda.time.DateTime;

import com.madalla.bo.SiteLanguage;
import com.madalla.cms.bo.impl.ocm.blog.Blog;
import com.madalla.cms.bo.impl.ocm.blog.BlogEntry;
import com.madalla.cms.bo.impl.ocm.image.Album;
import com.madalla.cms.bo.impl.ocm.image.Image;
import com.madalla.cms.bo.impl.ocm.image.ImageHelper;
import com.madalla.cms.bo.impl.ocm.page.Content;
import com.madalla.cms.bo.impl.ocm.page.ContentEntry;
import com.madalla.cms.bo.impl.ocm.page.Page;
import com.madalla.cms.bo.impl.ocm.page.PageMeta;
import com.madalla.cms.bo.impl.ocm.page.PageMetaLang;
import com.madalla.cms.bo.impl.ocm.page.Resource;
import com.madalla.cms.bo.impl.ocm.security.User;
import com.madalla.cms.service.ocm.RepositoryInfo.RepositoryType;



public class ContentOcmTest extends AbstractContentOcmTest{

	String keywords = "test key";

	public void testSiteData(){
		String parentPath = getApplicationNode();

		String name = getRandomName("site");
		Site site = new Site(parentPath, name);

		site.setMetaKeywords(keywords);

		ocm.insert(site);
		ocm.save();

		Site result = (Site) ocm.getObject(Site.class, site.getId());
		assertNotNull(result);
		assertEquals(site.getMetaKeywords(), keywords);

		System.out.println(result);
	}

	public void testOcmBlogEntry() throws RepositoryException{

		String blogsPath = getCreateParentNode(RepositoryType.BLOG);

		String blogName = getRandomName("Blog");
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

	public void testContentOcmImages() throws IOException{
		String parentPath = getCreateParentNode(RepositoryType.ALBUM);

		String name = getRandomName("Album");
		String nodePath ;
		{
			//Test Album
			Album album = new Album(parentPath, name);
			nodePath = album.getId();
			String title = "Test Ocm Image";
			album.setTitle(title);
			album.setType("testType");
			log.debug("inserting album :"+album.getId());
			ocm.insert(album);
			ocm.save();
			Album testResult = (Album) ocm.getObject(Album.class, album.getId());
			assertNotNull(testResult);
			assertEquals(album.getTitle(), title);
		}

		{
			//test Image
			InputStream jpg = this.getClass().getResourceAsStream("test1.jpg");
			InputStream png = this.getClass().getResourceAsStream("test1.png");
			Album album = (Album) ocm.getObject(Album.class, nodePath);


			Image image = new Image(album,"testjpg");
			ocm.insert(image);
			ocm.save();
			ImageHelper.saveImageFull(jcrTemplate, image.getId(), jpg);
			ImageHelper.saveImageThumb(jcrTemplate, image.getId());

			Image testImage = (Image) ocm.getObject(Image.class, image.getId());
			assertNotNull(testImage);
			testImage.setTitle("new title");
			ocm.update(testImage);
			ocm.save();

			Image postTest = (Image) ocm.getObject(Image.class, image.getId());
			assertNotNull(postTest);
			assertNotNull(postTest.getImageThumb());

		}

		ocm.remove(nodePath);
		ocm.save();

	}
	public void testContentOcmResource() throws IOException{
		String parentPath = getCreateParentNode(RepositoryType.RESOURCE);

		String nodePath ;
		{
	        String name = getRandomName("Page");
	        Page page = new Page(parentPath, name);
	        nodePath = page.getId();
	        ocm.insert(page);
	        ocm.save();

	        Page testPage = (Page) ocm.getObject(Page.class, page.getId());
	        InputStream pdf = this.getClass().getResourceAsStream("EugeneMalan.pdf");

	        Resource resource = new Resource(testPage, "testpdf", pdf);
	        ocm.insert(resource);
	        ocm.save();

	        Resource testResource = (Resource) ocm.getObject(Resource.class, resource.getId());
	        assertNotNull(testResource);
	        //assertNotNull(testResource.getInputStream());
	        //assertNotNull(testResource.getResource());

		}

		ocm.remove(nodePath);
		ocm.save();

	}

    public void testPageContentAndMeta(){
        String parentPath = getCreateParentNode(RepositoryType.PAGE);

        String name = getRandomName("Page");

        Page page = new Page(parentPath, name);
        ocm.insert(page);
        ocm.save();

        {   //Content
        	Page testPage = (Page) ocm.getObject(Page.class, page.getId());

        	Content content = new Content(testPage, "test");
        	content.setText("Content goes here");
        	ocm.insert(content);
        	ocm.save();

        	ContentEntry contentEntry = new ContentEntry(content, "english");
        	contentEntry.setText("Editable Content");
        	ocm.insert(contentEntry);
        	ocm.save();
        }

        {
			//Meta information
			Page testPage = (Page) ocm.getObject(Page.class, page.getId());
			PageMeta pageMeta = new PageMeta(testPage);
			ocm.insert(pageMeta);
			ocm.save();
			log.debug(pageMeta);
			PageMeta testPageMeta = (PageMeta) ocm.getObject(PageMeta.class, pageMeta.getId());
			assertNotNull(testPageMeta);

			PageMetaLang en = new PageMetaLang(testPageMeta, SiteLanguage.ENGLISH.getDisplayName());
			String dataen = "testen";
			en.setAuthor(dataen);
			en.setTitle(dataen);
			en.setDescription(dataen);
			ocm.insert(en);
			ocm.save();
			log.debug(en);
			PageMetaLang testEn = (PageMetaLang) ocm.getObject(PageMetaLang.class, en.getId());
			assertNotNull(testEn);
			assertEquals(dataen, testEn.getDescription());

		}

    }
	public void testUser(){
		String parentPath = getCreateParentNode(RepositoryType.USER);
		User user = new User(parentPath,getRandomName("User"));
		ocm.insert(user);
		ocm.save();

		User test = (User) ocm.getObject(User.class, user.getId());
		assertNotNull(test);

		ocm.remove(test);
		ocm.save();
	}


}
