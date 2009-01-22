package com.madalla.service.cms.ocm.page;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.madalla.cms.bo.impl.ocm.page.Content;
import com.madalla.cms.bo.impl.ocm.page.Page;
import com.madalla.cms.service.ocm.RepositoryInfo.RepositoryType;
import com.madalla.service.cms.ocm.AbstractContentOcmTest;

public class ContentOcmPageTest extends AbstractContentOcmTest{

    private Log log = LogFactory.getLog(this.getClass());
    
    public void testPageContent(){
        String parentPath = getCreateParentNode(RepositoryType.PAGE);
        
        String name = getRandomName("Page");
        
        Page page = new Page(parentPath, name);
        ocm.insert(page);
        ocm.save();
        
        Page testPage = (Page) ocm.getObject(Page.class, page.getId());
        
        Content content = new Content(testPage, "test");
        content.setText("Content goes here");
        ocm.insert(content);
        ocm.save();
        
        
        
    }

}
