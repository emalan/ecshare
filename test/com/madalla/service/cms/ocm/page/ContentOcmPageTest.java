package com.madalla.service.cms.ocm.page;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;

import com.madalla.service.cms.ocm.AbstractContentOcmTest;
import com.madalla.service.cms.ocm.RepositoryInfo;
import com.madalla.service.cms.ocm.RepositoryInfo.RepositoryType;

public class ContentOcmPageTest extends AbstractContentOcmTest{

    private Log log = LogFactory.getLog(this.getClass());
    
    public void testPageContent(){
        String parentPath = getCreateParentNode();
        
        String name = RandomStringUtils.randomAlphabetic(5)+"Page";
        
        Page page = new Page(parentPath, name);
        ocm.insert(page);
        ocm.save();
        
        Page testPage = (Page) ocm.getObject(Page.class, page.getId());
        
        Content content = new Content(testPage, "test");
        content.setText("Content goes here");
        ocm.insert(content);
        ocm.save();
        
        
        
    }

    
    private String getCreateParentNode(){
        return (String) template.execute(new JcrCallback(){

            public Object doInJcr(Session session) throws IOException,
                    RepositoryException {
                
                Node blogs = RepositoryInfo.getGroupNode(session, NS_TEST, RepositoryType.PAGE);
                session.save();
                return blogs.getPath();
            }
            
        });
    }
}
