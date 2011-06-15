package com.madalla.image;

import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;

abstract class ImageScaleProcessor {
	InputStream process(InputStream inputStream, LoggingImageObserver observer,
			int width, int height){

	    observer.logInfo("ImageScaleProcessor starting process.");
		BufferedImage bufferedImage;
		try {
//			if (inputStream instanceof LazyFileInputStream){
//				LazyFileInputStream fileInputStream = (LazyFileInputStream) inputStream;
//				fileInputStream.reset();
//			}
			observer.logInfo("ImageIO.read inputstream="+inputStream.getClass().getName());
			bufferedImage = ImageIO.read(inputStream);
			if (bufferedImage == null){
				throw new RuntimeException("scaleImage - ImagIO unable to read inputStream.");
			}
			observer.logInfo("ImageScaleProcessor calling processScaling.");
			BufferedImage scaledImage = processScaling(bufferedImage, width, height, observer);

			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageOutputStream imageOut = ImageIO.createImageOutputStream(out);

			ImageIO.write(scaledImage, "JPG", imageOut);
			out.flush();
			out.close();

			return new ByteArrayInputStream(out.toByteArray());
		} catch (Exception e) {
			observer.logError("scaleImage - failed.", e);
			return new ByteArrayInputStream(new byte[] {});
		}
	}

	abstract BufferedImage processScaling(BufferedImage bufferedImage,
			int targetWidth, int targetHeight, ImageObserver imageObserver);
}
