package com.madalla.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.jcr.RepositoryException;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.joda.time.DateTime;

import com.madalla.service.cms.BlogEntry;
import com.madalla.service.cms.Content;
import com.madalla.service.cms.IRepositoryAdminService;
import com.madalla.service.cms.IRepositoryService;

public class ContentServiceIntegrationTest extends  AbstractSpringWicketTester{

	Log log = LogFactory.getLog(this.getClass());
	private IRepositoryService contentService;
    private IRepositoryAdminService repositoryAdminService;
	private final static String CONTENT_ID = "testContent";
	private final static String CONTENT_PARENT = "testParentNode";
	private final static String CONTENT_TEXT = "Content text";
	
	protected List<String> getTestConfigLocations() {
		List<String> configLocations = new ArrayList<String>();
		configLocations.add("classpath:com/madalla/service/cms/applicationContext-cms.xml");
        configLocations.add("classpath:com/madalla/util/jcr/applicationContext-jcr-local.xml");
        
		return configLocations;
	}
	
    public void testContentBackup(){
        repositoryAdminService.backupContentRoot();
        repositoryAdminService.backupContentSite();
		List <File> files = repositoryAdminService.getBackupFileList();
		for (int i = 0; i < files.size(); i++) {
			if ("test-backup.xml".equals(files.get(i).getName())){
				repositoryAdminService.restoreContentSite(files.get(i));
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
    	Content content = new Content(CONTENT_PARENT,CONTENT_ID );
    	content.setText(CONTENT_TEXT);
    	contentService.setContent(content);
    	
    	String text = contentService.getContentData(CONTENT_PARENT, CONTENT_ID);
    	assertEquals(CONTENT_TEXT, text);
    }

    public void testContentGetSetLocale() throws RepositoryException{
    	String french = CONTENT_TEXT + "french stuff";
    	Content content = new Content(CONTENT_PARENT, contentService.getLocaleId(CONTENT_ID, Locale.FRENCH));
    	content.setText(french);
    	contentService.setContent(content);
    	
    	String text = contentService.getContentData(CONTENT_PARENT, CONTENT_ID, Locale.FRENCH);
    	assertEquals(text, french);
    }
    
    public void testBlogGetSet(){
    	BlogEntry blogEntry = createBlogEntry();
        String path = contentService.insertBlogEntry(blogEntry);
        BlogEntry testBlogEntry = contentService.getBlogEntry(path);
    	assertEquals(blogEntry.getBlog(), testBlogEntry.getBlog());
        assertEquals(blogEntry.getTitle(), testBlogEntry.getTitle());
        assertEquals(blogEntry.getText(), testBlogEntry.getText());
        //assertEquals(blogEntry.getDate(), testBlogEntry.getDate());
        
        contentService.deleteNode(path);
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
