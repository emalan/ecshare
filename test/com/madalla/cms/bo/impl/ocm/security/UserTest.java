package com.madalla.cms.bo.impl.ocm.security;

import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.springmodules.jcr.JcrCallback;

import com.madalla.cms.service.ocm.RepositoryInfo;
import com.madalla.cms.service.ocm.RepositoryInfo.RepositoryType;
import com.madalla.service.cms.ocm.AbstractContentOcmTest;

public class UserTest extends AbstractContentOcmTest{

	public void testUser(){
		String parentPath = getCreateParentNode();
		User user = new User(parentPath,"test1");
		ocm.insert(user);
		ocm.save();
		
		User test = (User) ocm.getObject(User.class, user.getId());
	}
	
    private String getCreateParentNode(){
        return (String) template.execute(new JcrCallback(){

            public Object doInJcr(Session session) throws IOException,
                    RepositoryException {
                
                Node parent = RepositoryInfo.getGroupNode(session, NS_TEST, RepositoryType.USER);
                session.save();
                return parent.getPath();
            }
            
        });
    }
}
