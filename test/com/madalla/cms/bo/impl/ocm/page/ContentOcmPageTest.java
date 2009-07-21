package com.madalla.cms.bo.impl.ocm.page;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.madalla.cms.bo.impl.ocm.AbstractContentOcmTest;
import com.madalla.cms.service.ocm.RepositoryInfo.RepositoryType;

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
        
        ContentEntry contentEntry = new ContentEntry(content, "english");
        contentEntry.setText("Editable Content");
        ocm.insert(contentEntry);
        ocm.save();
        
        
        
    }

}
