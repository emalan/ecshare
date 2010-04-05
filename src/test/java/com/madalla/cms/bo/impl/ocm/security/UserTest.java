package com.madalla.cms.bo.impl.ocm.security;

import com.madalla.cms.bo.impl.ocm.AbstractContentOcmTest;
import com.madalla.cms.service.ocm.RepositoryInfo.RepositoryType;

public class UserTest extends AbstractContentOcmTest{

	public void testUser(){
		String parentPath = getCreateParentNode(RepositoryType.USER);
		User user = new User(parentPath,getRandomName("User"));
		ocm.insert(user);
		ocm.save();
		
		User test = (User) ocm.getObject(User.class, user.getId());
		assertNotNull(test);
		
		ocm.remove(test);
		ocm.save();
	}
	
}
