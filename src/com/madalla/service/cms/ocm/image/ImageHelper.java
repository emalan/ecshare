package com.madalla.service.cms.ocm.image;

import java.io.IOException;
import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.WicketRuntimeException;
import org.springmodules.jcr.JcrCallback;

import com.madalla.service.cms.jcr.ImageData;

/**
 * Responsible for storing Originals 
 * 
 * @author exmalan
 *
 */
public class ImageHelper {
    private static final int MAX_WIDTH = 900;
    private static final int MAX_HEIGHT = 600;
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 266;
    private static final int THUMB_WIDTH = 90;
    private static final int THUMB_HEIGHT = 60;
    
    private static final String EC_NODE_ORIGINALS = "originals";
    
    private static final Log log = LogFactory.getLog(ImageHelper.class);
	
    private static ImageHelper instance;
	
    public static ImageHelper getInstance(){
		return instance;
	}
    
//	public String saveOriginalImage(final String name, final InputStream fullImage ){
//        ImageData imageData = new ImageData(EC_ORIGINALS, name, fullImage);
//        String path = save(imageData);
//        createThumbnail(path);
//        return path;
//    }
//	
//    public String saveAlbumImage(final String album, final String name){
//    	if (StringUtils.isEmpty(name)){
//    		throw new WicketRuntimeException("Album Image Name needs to be supplied.");
//    	}
//    	return (String)template.execute(new JcrCallback(){
//			public Object doInJcr(Session session) throws IOException, RepositoryException {
//				Node siteNode = getSiteNode(session);
//	        	Node parent = getParentNode(siteNode);
//	        	Node groupNode = getCreateNode(NS+EC_ORIGINALS, parent);
//				Node node = (Node) groupNode.getNode(NS+name);
//				InputStream fullImage = node.getProperty(EC_IMAGE_FULL).getStream();
//				InputStream albumImage = scaleAlbumImage(fullImage);
//				ImageData imageData = new ImageData(album, name, albumImage);
//				return save(imageData);
//			}
//		});
//    }
}
