package com.madalla.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.jcr.RepositoryException;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.tree.LinkTree;

import com.madalla.service.cms.BlogEntry;
import com.madalla.service.cms.Content;
import com.madalla.service.cms.IContentAdminService;
import com.madalla.service.cms.IContentService;

public class ContentServiceIntegrationTest extends  AbstractSpringWicketTester{

	Log log = LogFactory.getLog(this.getClass());
	private IContentService contentService;
    private IContentAdminService contentAdminService;
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
        contentAdminService.backupContentRoot();
        contentAdminService.backupContentSite();
		List <File> files = contentAdminService.getBackupFileList();
		for (int i = 0; i < files.size(); i++) {
			if ("test-backup.xml".equals(files.get(i).getName())){
				contentAdminService.restoreContentSite(files.get(i));
			}
		}

		if (contentAdminService.isRollbackApplicationAvailable()){
			contentAdminService.rollbackApplicationRestore();
		}
		if (contentAdminService.isRollbackSiteAvailable()){
			contentAdminService.rollbackSiteRestore();
		}

    }

    public void testContentExplorer(){
    	TreeModel treeModel = contentAdminService.getSiteContent();
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
    	Content content = new Content(CONTENT_PARENT, CONTENT_ID);
    	content.setText(french);
    	contentService.setContent(content, Locale.FRENCH);
    	
    	String text = contentService.getContentData(CONTENT_PARENT, CONTENT_ID, Locale.FRENCH);
    	assertEquals(text, french);
    }
    
    public void testBlogGetSet(){
    	BlogEntry blogEntry = createBlogEntry();
        String path = contentService.insertBlogEntry(blogEntry);
        BlogEntry testBlogEntry = contentService.getBlogEntry(path);
    	assertEquals(blogEntry, testBlogEntry);
        assertEquals(blogEntry.getTitle(), testBlogEntry.getTitle());
        
        contentService.deleteNode(path);
    }
    
    
    private final static String BLOG = "testBlog";
    private final static String BLOGCATEGORY = "testCategory";
    private final static String BLOGDESCRIPTION = "Blog Description Test";
    private final static String BLOGKEYWORDS = "test, keyword";
    private final static String BLOGTEXT = "test text";
    private final static String BLOGTITLE = "test title";
    private final static Date BLOGDATE = new Date();
    
    private BlogEntry createBlogEntry(){
    	BlogEntry entry = new BlogEntry.Builder(BLOG, BLOGTITLE, BLOGDATE).category(BLOGCATEGORY).build();
    	entry.setDescription(BLOGDESCRIPTION);
    	entry.setKeywords(BLOGKEYWORDS);
    	entry.setText(BLOGTEXT);
    	return entry;    	
    }

    public void setContentService(IContentService contentService) {
		this.contentService = contentService;
	}

    public void setContentAdminService(IContentAdminService contentAdminService) {
        this.contentAdminService = contentAdminService;
    }



}
