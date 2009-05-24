package com.madalla.cms.bo.impl.ocm.image;

import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;

import com.madalla.image.ImageUtilities;
import com.madalla.image.LoggingImageObserver;

/**
 * Utility class for processing Images 
 * 
 * @author Eugene Malan
 *
 */
public class ImageHelper {
    private static final int MAX_WIDTH = 900;
    private static final int MAX_HEIGHT = 600;
    private static final int DEFAULT_WIDTH = 400;
    private static final int DEFAULT_HEIGHT = 266;
    private static final int THUMB_WIDTH = 90;
    private static final int THUMB_HEIGHT = 60;
    
    private static final Log log = LogFactory.getLog(ImageHelper.class);
	
	private static final String IMAGE_FULL = "imageFull";
	private static final String IMAGE_THUMB = "imageThumb";
	
	public static void resizeAlbumImage(JcrTemplate template, final String path){
	    //TODO use default sizes setup in album
	    //TODO resize all images in album
	    template.execute(new JcrCallback(){

            public Object doInJcr(Session session) throws RepositoryException {
                Node node = (Node) session.getItem(path);
                InputStream inputStream = node.getProperty(IMAGE_FULL).getStream();
                node.setProperty(IMAGE_FULL, scaleAlbumImage(inputStream));
                session.save();
                return null;
            }
	        
	    });
	}
	
	public static void saveImageFull(JcrTemplate template, final String path, final InputStream inputStream){
	    log.info("saveImageFull - path="+path);
	        template.execute(new JcrCallback(){
	            public Object doInJcr(Session session) throws RepositoryException {
	                Node node = (Node) session.getItem(path);
	                InputStream scaled = scaleOriginalImage(inputStream);
	                node.setProperty(IMAGE_FULL, scaled);
	                session.save();
	                return null;
	            }
	        });
	}

	public static void saveImageThumb(JcrTemplate template, final String path){
        template.execute(new JcrCallback(){
            public Object doInJcr(Session session) throws RepositoryException {
                Node node = (Node) session.getItem(path);
                InputStream fullImage = node.getProperty(IMAGE_FULL).getStream();
                InputStream thumb = scaleThumbnailImage(fullImage);
                node.setProperty(IMAGE_THUMB, thumb);
                session.save();
                return null;
            }
        });
	}

   private static InputStream scaleOriginalImage(InputStream inputStream){
        LoggingImageObserver observer = new LoggingImageObserver(log);
        return ImageUtilities.scaleImageDownProportionately(inputStream, observer, MAX_WIDTH, MAX_HEIGHT);
    }
    
    private static InputStream scaleThumbnailImage(InputStream inputStream){
        LoggingImageObserver observer = new LoggingImageObserver(log);
        return ImageUtilities.scaleImageDownProportionately(inputStream, observer, THUMB_WIDTH, THUMB_HEIGHT);
    }
    
    private static InputStream scaleAlbumImage(InputStream inputStream){
        LoggingImageObserver observer = new LoggingImageObserver(log);
        return ImageUtilities.scaleImageDownProportionately(inputStream, observer, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

}
