package com.madalla.image;

import java.awt.Image;
import java.awt.image.ImageObserver;

import org.apache.commons.logging.Log;

/**
 * Reports progress of Image Processing to Log
 * <p>
 * TODO Link this up to some GUI progress indicator
 * </p>
 * @author exmalan
 *
 */
public class LoggingImageObserver implements ImageObserver {

	private final Log log;
	private int width;
	private int height;

	public LoggingImageObserver(Log log){
		this.log = log;
	}
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		this.width = width;
		this.height = height;
		//log.debug("imageUpdate - infoflags=" + infoflags);
		log.debug("Image size status : width=" + width + ", height=" + height);
		//log.debug("imageUpdate - x=" + x);
		//log.debug("imageUpdate - y=" + y);
		return false;
	}
	public void logError(String message, Exception e){
		log.error(message, e);
	}
	public void logInfo(String message){
		log.info(message);
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}

}
