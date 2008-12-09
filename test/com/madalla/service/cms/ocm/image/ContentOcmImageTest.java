package com.madalla.service.cms.ocm.image;

import java.io.IOException;
import java.io.InputStream;

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

public class ContentOcmImageTest extends AbstractContentOcmTest{

	private Log log = LogFactory.getLog(this.getClass());
	
	public void testContentOcmImages() throws IOException{
		String parentPath = getCreateParentNode();
		
		String name = RandomStringUtils.randomAlphabetic(5)+"Album";
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

			
			Image image = new Image(album,"testjpg",jpg);
			ocm.insert(image);
			ocm.save();
			ImageHelper.saveImageFull(template, image.getId(), jpg);
			ImageHelper.saveImageThumb(template, image.getId());
			
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
	
	private String getCreateParentNode(){
		return (String) template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				
				Node blogs = RepositoryInfo.getGroupNode(session, NS_TEST, RepositoryType.ALBUM);
				session.save();
				return blogs.getPath();
			}
			
		});
	}
}
