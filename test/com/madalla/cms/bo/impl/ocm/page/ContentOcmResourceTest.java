package com.madalla.cms.bo.impl.ocm.page;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.DynamicWebResource;

import com.madalla.cms.bo.impl.ocm.AbstractContentOcmTest;
import com.madalla.cms.bo.impl.ocm.image.Album;
import com.madalla.cms.bo.impl.ocm.image.Image;
import com.madalla.cms.bo.impl.ocm.image.ImageHelper;
import com.madalla.cms.service.ocm.RepositoryInfo.RepositoryType;
import com.sun.org.apache.regexp.internal.RESyntaxException;

public class ContentOcmResourceTest extends AbstractContentOcmTest{

	private Log log = LogFactory.getLog(this.getClass());
	
	public void testContentOcmResource() throws IOException{
		String parentPath = getCreateParentNode(RepositoryType.RESOURCE_PDF);
		
		String nodePath ;
		{
	        String name = getRandomName("Page");
	        Page page = new Page(parentPath, name);
	        nodePath = page.getId();
	        ocm.insert(page);
	        ocm.save();
	        
	        Page testPage = (Page) ocm.getObject(Page.class, page.getId());
	        InputStream pdf = this.getClass().getResourceAsStream("EugeneMalan.pdf");
	        
	        ResourcePdf resource = new ResourcePdf(testPage, "testpdf", pdf);
	        ocm.insert(resource);
	        ocm.save();
	        
	        ResourcePdf testResource = (ResourcePdf) ocm.getObject(ResourcePdf.class, resource.getId());
	        assertNotNull(testResource);
	        assertNotNull(testResource.getInputStream());
	        assertNotNull(testResource.getResource());
	        
		}
		
		ocm.remove(nodePath);
		ocm.save();
		
	}
	
}
