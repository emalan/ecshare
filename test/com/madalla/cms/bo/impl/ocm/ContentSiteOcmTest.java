package com.madalla.cms.bo.impl.ocm;



public class ContentSiteOcmTest extends AbstractContentOcmTest{

	String keywords = "test key";
	
	public void testSiteData(){
		String parentPath = getApplicationNode();
		
		String name = getRandomName("site");
		Site site = new Site(parentPath, name);
		
		site.setMetaKeywords(keywords);
		
		ocm.insert(site);
		ocm.save();
		
		Site result = (Site) ocm.getObject(Site.class, site.getId());
		assertNotNull(result);
		assertEquals(site.getMetaKeywords(), keywords);
		
		System.out.println(result);
	}
}
