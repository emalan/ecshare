package com.madalla;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.jcr.RepositoryException;
import javax.swing.tree.TreeModel;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.joda.time.DateTime;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.bo.AlbumData;
import com.madalla.bo.BlogEntryData;
import com.madalla.bo.ContentData;
import com.madalla.bo.IAlbumData;
import com.madalla.bo.IBlogData;
import com.madalla.bo.IBlogEntryData;
import com.madalla.bo.IImageData;
import com.madalla.bo.IPageData;
import com.madalla.bo.ImageData;
import com.madalla.bo.security.UserData;
import com.madalla.cms.bo.impl.ocm.image.Image;
import com.madalla.cms.bo.impl.ocm.page.Content;
import com.madalla.service.BackupFile;
import com.madalla.service.IRepositoryAdminService;
import com.madalla.service.IRepositoryService;

public class ContentServiceIntegrationTest extends  AbstractSpringWicketTester{

	Log log = LogFactory.getLog(this.getClass());
	private IRepositoryService contentService;
    private IRepositoryAdminService repositoryAdminService;
    protected JcrTemplate template;
	private final static String SITE = "test";
	
	protected List<String> getTestConfigLocations() {
		List<String> configLocations = new ArrayList<String>();
		configLocations.add("classpath:com/madalla/cms/service/ocm/applicationContext-cms.xml");
        configLocations.add("classpath:com/madalla/cms/jcr/applicationContext-jcr-local.xml");
        
		return configLocations;
	}
	
    public void testContentBackup(){
        repositoryAdminService.backupContentRoot();
        repositoryAdminService.backupContentSite();
		List <BackupFile> files = repositoryAdminService.getBackupFileList();
		for (int i = 0; i < files.size(); i++) {
			if ("test-backup.xml".equals(files.get(i).getName())){
				repositoryAdminService.restoreContentSite(files.get(i).getFile());
			}
		}

		if (repositoryAdminService.isRollbackApplicationAvailable()){
			repositoryAdminService.rollbackApplicationRestore();
		}
		if (repositoryAdminService.isRollbackSiteAvailable()){
			repositoryAdminService.rollbackSiteRestore();
		}

    }

    public void testContentExplorer(){
    	TreeModel treeModel = repositoryAdminService.getSiteContent();
    	assertNotNull(treeModel);
    	
    	LinkTree tree = new LinkTree("test", treeModel);
    	
    	appTester.startComponent(tree);
    	//tree.render();
    	//int size = tree.size();
    	//tree.g
    	//tree.render();
    	
    }
    
    private final static String CONTENT_ID = "testContent";
    private final static String CONTENT_PARENT = "testParentNode";

    public void testContentGetSet() throws RepositoryException{
    	String contentId = RandomStringUtils.randomAlphabetic(5);
    	String contentText = RandomStringUtils.randomAlphabetic(20);
    	
    	//test new
    	IPageData page = contentService.getPage(CONTENT_PARENT);
    	Content content = new Content(page,contentId );
    	String text = RandomStringUtils.randomAlphanumeric(20);
    	content.setText(text);
    	contentService.saveContent(content);
    	String testText = contentService.getContentText(page, contentId);
    	assertEquals(text, testText);
    
    	//test Update
    	{
    		ContentData c = contentService.getContent(page, contentId);
    		String s = "test1";
    		c.setText(s);
    		contentService.saveContent(c);
    		String s1 = contentService.getContentText(page, contentId);
    		assertEquals(s, s1);
    	}
    }

    public void testContentGetSetLocale() throws RepositoryException{
    	String french = RandomStringUtils.randomAlphanumeric(20) + "french stuff";
    	IPageData page = contentService.getPage(CONTENT_PARENT);
    	Content content = new Content(page, contentService.getLocaleId(CONTENT_ID, Locale.FRENCH));
    	content.setText(french);
    	contentService.saveContent(content);
    	
    	String text = contentService.getContentText(page, CONTENT_ID, Locale.FRENCH);
    	assertEquals(text, french);
    }
    
    public void testImageGetSet() throws FileNotFoundException{
    	InputStream stream = this.getClass().getResourceAsStream("test1.jpg");
    	assertNotNull(stream);
    	
    	final String name = "image1";
    	IAlbumData originalAlbum = contentService.getOriginalsAlbum();
    	String imagePath = contentService.createImage(originalAlbum, name, stream);
    	
    	AlbumData album;
    	{
    		album = contentService.getAlbum("testAlbum");
    		IImageData image = contentService.getImage(imagePath);
    		contentService.addImageToAlbum(album, image.getName());
    	}
    	{
    		IImageData testImage = contentService.getImage(imagePath);
    	    assertNotNull(testImage);
    	}
    	{
    	    List<ImageData> list = contentService.getAlbumImages(album);
    	    assertNotNull(list);
    	    assertTrue(list.size() > 0);
    	    Image image = (Image)list.get(0);
    	    assertNotNull(image);
    	    assertNotNull(image.getImageFull());
    	    assertNotNull(image.getImageThumb());
    	}
    	{
    		List<ImageData> list = contentService.getAlbumOriginalImages();
    	    assertNotNull(list);
    	    assertTrue(list.size() > 0);
    	}
    	TreeModel treeModel =  contentService.getAlbumImagesAsTree(album);
    	assertNotNull(treeModel);
    	assertTrue(treeModel.getChildCount(treeModel.getRoot()) >= 1);
    }
    
    private final static String BLOG = "testBlog";
    private final static String BLOGCATEGORY = "testCategory";
    private final static String BLOGDESCRIPTION = "Blog Description Test";
    private final static String BLOGKEYWORDS = "test, keyword";
    private final static String BLOGTEXT = "test text";
    private final static String BLOGTITLE = "test title";
    private final static DateTime BLOGDATE = new DateTime();
    
    public void testBlogGetSet(){
    	IBlogData blog = contentService.getBlog(BLOG);
    	assertNotNull(blog);
    	BlogEntryData blogEntry = contentService.getNewBlogEntry(blog, BLOGTITLE, BLOGDATE);
    	assertNotNull(blogEntry);
    	blogEntry.setDescription(BLOGDESCRIPTION);
    	String path = blogEntry.getId();
    	contentService.saveBlogEntry(blogEntry);
    	
    	IBlogEntryData testEntry = contentService.getBlogEntry(path);
    	assertNotNull(testEntry);
    	
    	contentService.deleteNode(path);
    }
    
    public void testUserCreate(){
        String username = "test1User";
        String password = "password";
        UserData user = contentService.getNewUser(username, password);
        
        UserData test = contentService.getUser(username);
        assertNotNull(test);
        
        UserData nouser = contentService.getNewUser("nouser","pwd");
        assertNull(nouser);
        
        contentService.deleteNode(test.getId());
    }
    
    public void testUserGetAll(){
    	Collection<UserData> list = contentService.getUsers();
    	for(UserData user : list){
    		System.out.println(user);
    	}
    }
    
    public void setRepositoryService(IRepositoryService contentService) {
		this.contentService = contentService;
	}

    public void setRepositoryAdminService(IRepositoryAdminService contentAdminService) {
        this.repositoryAdminService = contentAdminService;
    }

	public JcrTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}


}
