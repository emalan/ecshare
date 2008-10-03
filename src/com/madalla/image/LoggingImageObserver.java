package com.madalla.image;

import java.awt.Image;
import java.awt.image.ImageObserver;

import org.apache.commons.logging.Log;

public class LoggingImageObserver implements ImageObserver {

	private final Log log;
	public LoggingImageObserver(Log log){
		this.log = log;
	}
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		log.debug("imageUpdate - infoflags=" + infoflags);
		log.debug("imageUpdate - width=" + width);
		log.debug("imageUpdate - height=" + height);
		log.debug("imageUpdate - x=" + x);
		log.debug("imageUpdate - y=" + y);
		return false;
	}
	public void logError(String message, Exception e){
		log.error(message, e);
	}
	public void logInfo(String message){
		log.info(message);
	}

}
