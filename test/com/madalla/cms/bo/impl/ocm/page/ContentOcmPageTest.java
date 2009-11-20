package com.madalla.cms.bo.impl.ocm.page;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.madalla.bo.SiteLanguage;
import com.madalla.cms.bo.impl.ocm.AbstractContentOcmTest;
import com.madalla.cms.service.ocm.RepositoryInfo.RepositoryType;

public class ContentOcmPageTest extends AbstractContentOcmTest{

    private Log log = LogFactory.getLog(this.getClass());
    
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

    
}
