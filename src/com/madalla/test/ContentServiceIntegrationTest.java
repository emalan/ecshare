package com.madalla.test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.jcr.RepositoryException;
import javax.swing.tree.TreeModel;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.joda.time.DateTime;

import com.madalla.service.cms.AbstractBlogEntry;
import com.madalla.service.cms.AbstractImageData;
import com.madalla.service.cms.BackupFile;
import com.madalla.service.cms.IRepositoryAdminService;
import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.jcr.BlogEntry;
import com.madalla.service.cms.jcr.Content;

public class ContentServiceIntegrationTest extends  AbstractSpringWicketTester{

	Log log = LogFactory.getLog(this.getClass());
	private IRepositoryService contentService;
    private IRepositoryAdminService repositoryAdminService;
	private final static String CONTENT_ID = "testContent";
	private final static String CONTENT_PARENT = "testParentNode";
	private final static String CONTENT_TEXT = "Content text";
	
	protected List<String> getTestConfigLocations() {
		List<String> configLocations = new ArrayList<String>();
		configLocations.add("classpath:com/madalla/service/cms/jcr/applicationContext-cms.xml");
        configLocations.add("classpath:com/madalla/util/jcr/applicationContext-jcr-local.xml");
        
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
    
    public void testContentGetSet() throws RepositoryException{
    	String contentId = RandomStringUtils.randomAlphabetic(5);
    	
    	//test new
    	Content content = new Content(CONTENT_PARENT,contentId );
    	String text = RandomStringUtils.randomAlphanumeric(20);
    	content.setText(text);
    	String path = content.save();
    	String testText = contentService.getContentData(CONTENT_PARENT, contentId);
    	assertEquals(text, testText);
    	
    	//TODO test update
    	Content existing = new Content(path, CONTENT_PARENT, contentId);
    	String updateText = RandomStringUtils.randomAlphanumeric(100);
    	existing.setText(updateText);
    	existing.save();
    	String testExisting = contentService.getContentData(CONTENT_PARENT, contentId);
    	assertEquals(updateText, testExisting);
    
    }

    public void testContentGetSetLocale() throws RepositoryException{
    	String french = RandomStringUtils.randomAlphanumeric(20) + "french stuff";
    	Content content = new Content(CONTENT_PARENT, contentService.getLocaleId(CONTENT_ID, Locale.FRENCH));
    	content.setText(french);
    	content.save();
    	
    	String text = contentService.getContentData(CONTENT_PARENT, CONTENT_ID, Locale.FRENCH);
    	assertEquals(text, french);
    }
    
    public void testBlogGetSet(){
    	BlogEntry blogEntry = createBlogEntry();
        String path = blogEntry.save();
        
        AbstractBlogEntry testBlogEntry = contentService.getBlogEntry(path);
        assertEquals(blogEntry, testBlogEntry);
        assertEquals(blogEntry.getBlog(), testBlogEntry.getBlog());
        assertEquals(blogEntry.getTitle(), testBlogEntry.getTitle());
        assertEquals(blogEntry.getText(), testBlogEntry.getText());
        //assertEquals(blogEntry.getDate(), testBlogEntry.getDate());
        
        contentService.deleteNode(path);
    }
    
    public void testImageGetSet() throws FileNotFoundException{
    	InputStream stream = this.getClass().getResourceAsStream("ContentServiceIntegrationTest.java");
    	assertNotNull(stream);
    	final String album = "testAlbum";
    	final String name = "image1";
    	String path = repositoryAdminService.createOriginalImage(name, stream);
    	
    	log.info("testImageGetSet - path="+path);
    	
    	AbstractImageData test = contentService.getImageData(path);
    	assertNotNull(test);
    	
    	String albumPath = repositoryAdminService.addImageToAlbum(album, name);
    	log.info("testAddImageToAlbum - path="+albumPath);
    	
    	TreeModel treeModel = contentService.getAlbumImagesAsTree(album);
    	assertNotNull(treeModel);
    	assertEquals(treeModel.getChildCount(treeModel.getRoot()), 1);
    }
    
    
    
    private final static String BLOG = "testBlog";
    private final static String BLOGCATEGORY = "testCategory";
    private final static String BLOGDESCRIPTION = "Blog Description Test";
    private final static String BLOGKEYWORDS = "test, keyword";
    private final static String BLOGTEXT = "test text";
    private final static String BLOGTITLE = "test title";
    private final static DateTime BLOGDATE = new DateTime();
    
    private BlogEntry createBlogEntry(){
    	BlogEntry entry = new BlogEntry.Builder(BLOG, BLOGTITLE, BLOGDATE).category(BLOGCATEGORY).build();
    	entry.setDescription(BLOGDESCRIPTION);
    	entry.setKeywords(BLOGKEYWORDS);
    	entry.setText(BLOGTEXT);
    	return entry;    	
    }

    public void setRepositoryService(IRepositoryService contentService) {
		this.contentService = contentService;
	}

    public void setRepositoryAdminService(IRepositoryAdminService contentAdminService) {
        this.repositoryAdminService = contentAdminService;
    }



}
