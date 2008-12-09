package com.madalla.service.cms.ocm.image;

import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.image.resource.BufferedDynamicImageResource;
import org.apache.wicket.markup.html.image.resource.DynamicImageResource;

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
	
	public static InputStream scaleOriginalImage(InputStream inputStream){
		LoggingImageObserver observer = new LoggingImageObserver(log);
		return ImageUtilities.scaleImageDownProportionately(inputStream, observer, MAX_WIDTH, MAX_HEIGHT);
	}
	
	public static InputStream scaleThumbnailImage(InputStream inputStream){
		LoggingImageObserver observer = new LoggingImageObserver(log);
		return ImageUtilities.scaleImageDownProportionately(inputStream, observer, THUMB_WIDTH, THUMB_HEIGHT);
	}
	
	public static InputStream scaleAlbumImage(InputStream inputStream){
		LoggingImageObserver observer = new LoggingImageObserver(log);
		return ImageUtilities.scaleImageDownProportionately(inputStream, observer, DEFAULT_WIDTH, DEFAULT_HEIGHT);
	}
	
	static DynamicImageResource createImageResource(InputStream inputStream){
		BufferedDynamicImageResource webResource = null;
		try {
			webResource = new BufferedDynamicImageResource();
			webResource.setImage(ImageIO.read(inputStream));

		} catch (Exception e) {
			log.error("Exception while reading image from Content.",e);
			//throw new WicketRuntimeException("Error while reading image from Content Management System.");
		}
		return webResource;
	}
	
}
