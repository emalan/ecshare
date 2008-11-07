package com.madalla.image;

import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

abstract class ImageScaleProcessor {
	InputStream process(InputStream inputStream, LoggingImageObserver observer,
			int width, int height){
		
		BufferedImage bufferedImage;
		try {
			bufferedImage = ImageIO.read(inputStream);
		
			BufferedImage scaledImage = processScaling(bufferedImage, width, height, observer);
		
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageOutputStream imageOut = ImageIO.createImageOutputStream(out);
		
			ImageIO.write(scaledImage, "JPG", imageOut);
			out.flush();
			out.close();
		
			return new ByteArrayInputStream(out.toByteArray());
		} catch (IOException e) {
			observer.logError("scaleImage - failed.", e);
			return new ByteArrayInputStream(new byte[] {});
		}
	}
	
	abstract BufferedImage processScaling(BufferedImage bufferedImage, 
			int targetWidth, int targetHeight, ImageObserver imageObserver);
}