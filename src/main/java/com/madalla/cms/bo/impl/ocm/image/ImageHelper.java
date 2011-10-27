package com.madalla.cms.bo.impl.ocm.image;

import java.io.InputStream;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final int THUMB_WIDTH = 90;
    private static final int THUMB_HEIGHT = 60;

    private static final Logger log = LoggerFactory.getLogger(ImageHelper.class);

	private static final String IMAGE_FULL = "imageFull";
	private static final String IMAGE_THUMB = "imageThumb";
	private static final String WIDTH = "imageWidth";
	private static final String HEIGHT = "imageHeight";

	public static void resizeAlbumImage(JcrTemplate template, final String path, final int width, final int height){
	    template.execute(new JcrCallback(){

            public Object doInJcr(Session session) throws RepositoryException {
                Node node = (Node) session.getItem(path);
                InputStream inputStream = node.getProperty(IMAGE_FULL).getStream();
                node.setProperty(IMAGE_FULL, scaleAlbumImage(inputStream, width, height));
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
	                LoggingImageObserver observer = new LoggingImageObserver(log);
	                InputStream scaled = scaleOriginalImage(inputStream, observer);
	                node.setProperty(IMAGE_FULL, scaled);
	                node.setProperty(WIDTH, observer.getWidth());
	                node.setProperty(HEIGHT, observer.getHeight());
	                session.save();
	                return null;
	            }
	        });
	}

	public static void saveImageThumb(JcrTemplate template, final String path){
		log.info("saveImageThumb - path="+path);
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

   private static InputStream scaleOriginalImage(InputStream inputStream, LoggingImageObserver observer){
        return ImageUtilities.scaleImageDownProportionately(inputStream, observer, MAX_WIDTH, MAX_HEIGHT);
    }

    private static InputStream scaleThumbnailImage(InputStream inputStream){
        LoggingImageObserver observer = new LoggingImageObserver(log);
        return ImageUtilities.scaleImageDownProportionately(inputStream, observer, THUMB_WIDTH, THUMB_HEIGHT);
    }

    private static InputStream scaleAlbumImage(InputStream inputStream, int width, int height){
        LoggingImageObserver observer = new LoggingImageObserver(log);
        return ImageUtilities.scaleImageDownProportionately(inputStream, observer, width, height );
    }

}
