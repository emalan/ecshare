package com.madalla;

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
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;
import org.joda.time.DateTime;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.service.cms.AbstractBlog;
import com.madalla.service.cms.AbstractImageData;
import com.madalla.service.cms.BackupFile;
import com.madalla.service.cms.IRepositoryAdminService;
import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.jcr.Content;
import com.madalla.service.cms.jcr.RepositoryService;
import com.madalla.service.cms.ocm.blog.BlogEntry;
import com.madalla.service.cms.ocm.image.Album;
import com.madalla.service.cms.ocm.image.Image;
import com.madalla.util.jcr.ocm.JcrOcmConversion;

public class ContentServiceIntegrationTest extends  AbstractSpringWicketTester{

	Log log = LogFactory.getLog(this.getClass());
	private IRepositoryService contentService;
    private IRepositoryAdminService repositoryAdminService;
    protected JcrTemplate template;
	private RepositoryService oldRepositoryService;
	private final static String SITE = "test";
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

    public void testImageGetSet() throws FileNotFoundException{
    	InputStream stream = this.getClass().getResourceAsStream("test1.jpg");
    	assertNotNull(stream);
    	
    	final String name = "image1";
    	Album originalAlbum = contentService.getOriginalsAlbum();
    	Image image = contentService.createImage(originalAlbum, name, stream);
    	log.info("testImageGetSet - path="+image.getId());
    	assertNotNull(image);

    	Album album;
    	{
    		album = contentService.getAlbum("testAlbum");
    		contentService.addImageToAlbum(album, image.getId());
    	}
    	{
    	    Image testImage = contentService.getImage(image.getId());
    	    assertNotNull(testImage);
    	}
    	{
    	    List<Image> list = contentService.getAlbumImages(album);
    	    assertNotNull(list);
    	    assertTrue(list.size() > 0);
    	}
    	{
    		List<Image> list = contentService.getAlbumOriginalImages();
    	    assertNotNull(list);
    	    assertTrue(list.size() > 0);
    	}
    	TreeModel treeModel =  contentService.getAlbumImagesAsTree(album);
    	assertNotNull(treeModel);
    	assertTrue(treeModel.getChildCount(treeModel.getRoot()) >= 1);
    }
    
    public void testImageConversion() throws ResourceStreamNotFoundException{
    	
    	final String album = "testAlbum";
    	final String name = "image";
    	for (int i = 1; i <= 3; i++){
    		InputStream stream = this.getClass().getResourceAsStream("test"+i+".jpg");
    		assertNotNull(stream);
    		String path = repositoryAdminService.createOriginalImage(name+i, stream);
    		log.info("testImageGetSet - path="+path);
    	}
    	
    	List<AbstractImageData> list = oldRepositoryService.getAlbumOriginalImages();
    	String testImage;
    	for(AbstractImageData oldImage: list){
    		Album originalAlbum = contentService.getOriginalsAlbum();
        	Image image = contentService.createImage(originalAlbum, oldImage.getName(), oldImage.getFullImageAsResource().getResourceStream().getInputStream());
        	log.info("testImageGetSet - path="+image.getId());
        	testImage = image.getId();
    	}
    	
//    	
//    	String albumPath = repositoryAdminService.addImageToAlbum(album, name);
//    	log.info("testAddImageToAlbum - path="+albumPath);
//    	
//    	TreeModel treeModel = contentService.getAlbumImagesAsTree(album);
//    	assertNotNull(treeModel);
//    	assertEquals(treeModel.getChildCount(treeModel.getRoot()), 1);
    	
    }
    
    private final static String BLOG = "testBlog";
    private final static String BLOGCATEGORY = "testCategory";
    private final static String BLOGDESCRIPTION = "Blog Description Test";
    private final static String BLOGKEYWORDS = "test, keyword";
    private final static String BLOGTEXT = "test text";
    private final static String BLOGTITLE = "test title";
    private final static DateTime BLOGDATE = new DateTime();
    
    public void testBlogGetSet(){
    	AbstractBlog blog = contentService.getBlog(BLOG);
    	assertNotNull(blog);
    	BlogEntry blogEntry = contentService.getNewBlogEntry(blog, BLOGTITLE, BLOGDATE);
    	assertNotNull(blogEntry);
    	blogEntry.setDescription(BLOGDESCRIPTION);
    	String path = blogEntry.getId();
    	contentService.saveBlogEntry(blogEntry);
    	
    	BlogEntry testEntry = contentService.getBlogEntry(path);
    	assertNotNull(testEntry);
    	
    	contentService.deleteNode(path);
    }
    
    public void testBlogConversion(){
    	//Create some old Blog Entries
    	for (int i = 0; i < 5; i++){
    		com.madalla.service.cms.jcr.BlogEntry blogEntry = createBlogEntry(i);
    		String path = blogEntry.save();
    	}
        
        //do conversion
    	JcrOcmConversion conversion = new JcrOcmConversion();
    	conversion.init(template, oldRepositoryService, contentService,SITE);
    	conversion.convertNodesToOcm();
    	
    	//test conversion
    }
    
    private com.madalla.service.cms.jcr.BlogEntry createBlogEntry(int i){
    	com.madalla.service.cms.jcr.BlogEntry entry = new com.madalla.service.cms.jcr.BlogEntry.Builder(BLOG, BLOGTITLE+i, BLOGDATE).category(BLOGCATEGORY).build();
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

	public RepositoryService getOldRepositoryService() {
		return oldRepositoryService;
	}

	public void setOldRepositoryService(RepositoryService oldRepositoryService) {
		this.oldRepositoryService = oldRepositoryService;
	}
    
	public JcrTemplate getTemplate() {
		return template;
	}

	public void setTemplate(JcrTemplate template) {
		this.template = template;
	}


}
