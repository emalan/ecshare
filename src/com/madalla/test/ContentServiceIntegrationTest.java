package com.madalla.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.jcr.RepositoryException;
import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.tree.LinkTree;

import com.madalla.service.cms.IContentService;
import com.madalla.webapp.cms.Content;

public class ContentServiceIntegrationTest extends  AbstractSpringWicketTester{

	Log log = LogFactory.getLog(this.getClass());
	private IContentService contentService;
	private final static String CONTENT_ID = "testContent";
	private final static String CONTENT_PARENT = "testParentNode";
	private final static String CONTENT_TEXT = "Content text";
	
	protected List getTestConfigLocations() {
		List configLocations = new ArrayList();
		configLocations.add("classpath:com/madalla/service/cms/applicationContext-cms.xml");
        configLocations.add("classpath:com/madalla/util/jcr/model/applicationContext-jcr.xml");
        
		return configLocations;
	}

    public void testContentExplorer(){
    	TreeModel treeModel = contentService.getSiteContent();
    	assertNotNull(treeModel);
    	
    	LinkTree tree = new LinkTree("test", treeModel);
    	
    	appTester.startComponent(tree);
    	//tree.render();
    	//int size = tree.size();
    	//tree.g
    	//tree.render();
    	
    }
    
    public void testContentGetSet() throws RepositoryException{
    	Content content = new Content();
    	content.setContentId(CONTENT_ID);
    	content.setPageName(CONTENT_PARENT);
    	content.setText(CONTENT_TEXT);
    	contentService.setContent(content);
    	
    	String text = contentService.getContentData(CONTENT_PARENT, CONTENT_ID);
    	assertEquals(CONTENT_TEXT, text);
    }

    public void testContentGetSetLocale() throws RepositoryException{
    	String french = CONTENT_TEXT + "french stuff";
    	Content content = new Content();
    	content.setContentId(CONTENT_ID);
    	content.setPageName(CONTENT_PARENT);
    	content.setText(french);
    	contentService.setContent(content, Locale.FRENCH);
    	
    	String text = contentService.getContentData(CONTENT_PARENT, CONTENT_ID, Locale.FRENCH);
    	assertEquals(text, french);
    }

    public void setContentService(IContentService contentService) {
		this.contentService = contentService;
	}



}
