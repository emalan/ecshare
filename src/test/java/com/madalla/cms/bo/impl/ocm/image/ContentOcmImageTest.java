package com.madalla.cms.bo.impl.ocm.image;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.madalla.cms.bo.impl.ocm.AbstractContentOcmTest;
import com.madalla.cms.service.ocm.RepositoryInfo.RepositoryType;

public class ContentOcmImageTest extends AbstractContentOcmTest{

	private Log log = LogFactory.getLog(this.getClass());
	
	public void testContentOcmImages() throws IOException{
		String parentPath = getCreateParentNode(RepositoryType.ALBUM);
		
		String name = getRandomName("Album");
		String nodePath ;
		{
			//Test Album
			Album album = new Album(parentPath, name);
			nodePath = album.getId();
			String title = "Test Ocm Image";
			album.setTitle(title);
			album.setType("testType");
			log.debug("inserting album :"+album.getId());
			ocm.insert(album);
			ocm.save();
			Album testResult = (Album) ocm.getObject(Album.class, album.getId());
			assertNotNull(testResult);
			assertEquals(album.getTitle(), title);
		}
		
		{
			//test Image
			InputStream jpg = this.getClass().getResourceAsStream("test1.jpg");
			InputStream png = this.getClass().getResourceAsStream("test1.png");
			Album album = (Album) ocm.getObject(Album.class, nodePath);

			
			Image image = new Image(album,"testjpg");
			ocm.insert(image);
			ocm.save();
			ImageHelper.saveImageFull(jcrTemplate, image.getId(), jpg);
			ImageHelper.saveImageThumb(jcrTemplate, image.getId());
			
			Image testImage = (Image) ocm.getObject(Image.class, image.getId());
			assertNotNull(testImage);
			testImage.setTitle("new title");
			ocm.update(testImage);
			ocm.save();
			
			Image postTest = (Image) ocm.getObject(Image.class, image.getId());
			assertNotNull(postTest);
			assertNotNull(postTest.getImageThumb());
			
		}
		
		ocm.remove(nodePath);
		ocm.save();
		
	}
	
}
