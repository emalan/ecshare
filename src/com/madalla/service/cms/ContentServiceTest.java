package com.madalla.service.cms;

import java.util.Locale;

import javax.jcr.RepositoryException;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import com.madalla.webapp.cms.Content;

public class ContentServiceTest extends  AbstractDependencyInjectionSpringContextTests{

	Log log = LogFactory.getLog(this.getClass());
	private IContentService contentService;
	private final static String CONTENT_ID = "testContent";
	private final static String CONTENT_PARENT = "testParentNode";
	private final static String CONTENT_TEXT = "Content text";
	
    protected void onSetUp() throws Exception {
    }

    protected String[] getConfigLocations() {
        
        return new String[]{
        		"classpath:com/madalla/service/cms/applicationContext-cms.xml",
                "classpath:com/madalla/service/cms/applicationContext-test.xml"};
    }
    
    public void testContentExplorer(){
    	TreeModel treeModel = contentService.getSiteContent();
    	assertNotNull(treeModel);
    }
    
    public void testContentGetSet() throws RepositoryException{
    	Content content = new Content();
    	content.setContentId(CONTENT_ID);
    	content.setClassName(CONTENT_PARENT);
    	content.setText(CONTENT_TEXT);
    	contentService.setContent(content);
    	
    	String text = contentService.getContentData(CONTENT_PARENT, CONTENT_ID);
    	assertEquals(CONTENT_TEXT, text);
    }

    public void testContentGetSetLocale() throws RepositoryException{
    	String french = CONTENT_TEXT + "french stuff";
    	Content content = new Content();
    	content.setContentId(CONTENT_ID);
    	content.setClassName(CONTENT_PARENT);
    	content.setText(french);
    	contentService.setContent(content, Locale.FRENCH);
    	
    	String text = contentService.getContentData(CONTENT_PARENT, CONTENT_ID, Locale.FRENCH);
    	assertEquals(text, french);
    }

    public void setContentService(IContentService contentService) {
		this.contentService = contentService;
	}

}
