package com.madalla.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Graphics Utilities class that provide default application methods.
 * 
 * @author Eugene Malan
 *
 */
public class ImageUtilities {
	
    private static Log log = LogFactory.getLog(ImageUtilities.class);
    @SuppressWarnings("unused")
	private static final String JPEG = "image/jpeg";
    @SuppressWarnings("unused")
	private static final String PNG = "image/png";
	@SuppressWarnings("unused")
	private static final String GIF = "image/gif";
    
    public static void validateImageIO(){
        log.info("ImageIO validation. If there is no Success message below, then validation has failed.");
        
        // This code crashes jvm in OpenJava !!!!
        //validateImageIO(JPEG);
        //validateImageIO(PNG);
        //validateImageIO(GIF);
        
        log.info("ImageIO reader formats :" + getImageIOReaderFormats());
        log.info("ImageIO reader MIME Types :" + getImageIOReaderMIMETypes());
        log.info("ImageIO writer formats :" + getImageIOWriterFormats());
        log.info("ImageIO writer MIME Types :" + getImageIOWriterMIMETypes());
        log.info("Success!! ImageIO validation completed.");
        
    }
    
    @SuppressWarnings("unused")
	private static void validateImageIO(String type){
        boolean found = false;
        log.info("ImageIO Reader validation. checking for MimeType="+ type);
        Iterator<ImageReader> iter = ImageIO.getImageReadersByMIMEType(type);
        while (iter.hasNext()) {
            found = true;
            ImageReader reader = iter.next();
            log.info("ImageIO Reader validation. Found reader.. "+ reader.getClass().getName());
        }
        if (!found){
            log.error("ImageIO - No readers found for type. Type="+type + ". Application may behave erratically when processing images of this type.");
        }
    }

    private static Collection<String> getImageIOReaderFormats(){
        String[] formatNames = ImageIO.getReaderFormatNames();
        return(unique(formatNames));
    }
    private static Collection<String> getImageIOWriterFormats(){
        String[] formatNames = ImageIO.getWriterFormatNames();
        return unique(formatNames);
    }
    private static Collection<String> getImageIOReaderMIMETypes(){
        String[] formatNames = ImageIO.getReaderMIMETypes();
        return unique(formatNames);
    }
    private static Collection<String> getImageIOWriterMIMETypes(){
        String[] formatNames = ImageIO.getWriterMIMETypes();
        return unique(formatNames);
    }
    
    private static Collection<String> unique(String[] strings) {
        Set<String> set = new HashSet<String>();
        for (int i=0; i<strings.length; i++) {
            String name = strings[i].toLowerCase();
            set.add(name);
        }
        return set;
    }

    
    public static InputStream scaleImageDownProportionately(InputStream inputStream, LoggingImageObserver observer,
			int maxWidth, int maxHeight){
		
	    log.debug("scaleImageDownProportionately - maxWidth="+maxWidth+" maxHeight="+maxHeight);
		
		ImageScaleProcessor processor = new ImageScaleProcessor(){
			
		    @Override
			BufferedImage processScaling(BufferedImage bufferedImage, int targetWidth, int targetHeight,
					ImageObserver imageObserver) {
				return getScaledDownProportionalInstance(bufferedImage, targetWidth, targetHeight, imageObserver);
			}
			
		};
		
		return processor.process(inputStream, observer, maxWidth, maxHeight);
	}
	

	/**
	 * @param bufferedImage
	 * @param targetWidth
	 * @param targetHeight
	 * @return BufferedImage that has been scaled to the specified height and width.
	 */
	@SuppressWarnings("unused")
	private static BufferedImage getScaledInstance(BufferedImage bufferedImage, 
			int targetWidth, int targetHeight,ImageObserver imageObserver){
		return getScaledInstance(bufferedImage, targetWidth, targetHeight, 
				RenderingHints.VALUE_INTERPOLATION_BICUBIC, true, imageObserver);
	}

	/**
	 * @param bufferedImage
	 * @param targetWidth
	 * @param targetHeight
	 * @return BufferedImage that has been scaled down proportionately to within the specified height and width.
	 */
	private static BufferedImage getScaledDownProportionalInstance(BufferedImage bufferedImage, 
			int targetWidth, int targetHeight, ImageObserver imageObserver){
		double actualWidth = bufferedImage.getWidth();
		double actualHeight = bufferedImage.getHeight();
		double scaleFactor = (double)targetWidth / actualWidth;
		if (actualHeight * scaleFactor > targetHeight){
			scaleFactor = targetHeight / actualHeight;
		}
		if (scaleFactor >= 1){
			imageObserver.imageUpdate(bufferedImage, 0, 0, 0, (int)actualWidth, (int)actualHeight);
			return bufferedImage;
		} 
		int adjWidth = (int) (scaleFactor * actualWidth);
		int adjHeight = (int) (scaleFactor * actualHeight);

		return getScaledInstance(bufferedImage, adjWidth, adjHeight, 
				RenderingHints.VALUE_INTERPOLATION_BICUBIC, true, imageObserver);
	}
	
	/**
	 * @param bufferedImage
	 * @param targetWidth
	 * @param targetHeight
	 * @return BufferedImage that has been scaled down proportionatly to within the specified height and width.
	 */
	@SuppressWarnings("unused")
	private static BufferedImage getScaledProportinalInstance(BufferedImage bufferedImage, 
			int targetWidth, int targetHeight, ImageObserver imageObserver){
		double actualWidth = bufferedImage.getWidth();
		double actualHeight = bufferedImage.getHeight();
		double scaleFactor = (double)targetWidth / actualWidth;
		if (actualHeight * scaleFactor > targetHeight){
			scaleFactor = targetHeight / actualHeight;
		}
		int adjWidth = (int) (scaleFactor * actualWidth);
		int adjHeight = (int) (scaleFactor * actualHeight);
		
		return getScaledInstance(bufferedImage, adjWidth, adjHeight, 
				RenderingHints.VALUE_INTERPOLATION_BICUBIC, true, imageObserver);
	}
	
	/**
     * Convenience method that returns a scaled instance of the
     * provided {@code BufferedImage}.
     *
     * @param img the original image to be scaled
     * @param targetWidth the desired width of the scaled instance,
     *    in pixels
     * @param targetHeight the desired height of the scaled instance,
     *    in pixels
     * @param hint one of the rendering hints that corresponds to
     *    {@code RenderingHints.KEY_INTERPOLATION} (e.g.
     *    {@code RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BILINEAR},
     *    {@code RenderingHints.VALUE_INTERPOLATION_BICUBIC})
     * @param higherQuality if true, this method will use a multi-step
     *    scaling technique that provides higher quality than the usual
     *    one-step technique (only useful in downscaling cases, where
     *    {@code targetWidth} or {@code targetHeight} is
     *    smaller than the original dimensions, and generally only when
     *    the {@code BILINEAR} hint is specified)
     * @return a scaled version of the original {@code BufferedImage}
     */
    private static BufferedImage getScaledInstance(BufferedImage img,
                                           int targetWidth,
                                           int targetHeight,
                                           Object hint,
                                           boolean higherQuality,
                                           ImageObserver observer)
    {
        int type = (img.getTransparency() == Transparency.OPAQUE) ?
            BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage ret = img;
        int w, h;
        if (higherQuality) {
            // Use multi-step technique: start with original size, then
            // scale down in multiple passes with drawImage()
            // until the target size is reached
            w = img.getWidth();
            h = img.getHeight();
            if (targetWidth >= w || targetHeight >= h){
                w = targetWidth;
                h = targetHeight;
            }
        } else {
            // Use one-step technique: scale directly from original
            // size to target size with a single drawImage() call
            w = targetWidth;
            h = targetHeight;
        }
        
        do {
            if (higherQuality && w > targetWidth) {
                w /= 2;
                if (w < targetWidth) {
                    w = targetWidth;
                }
            }

            if (higherQuality && h > targetHeight) {
                h /= 2;
                if (h < targetHeight) {
                    h = targetHeight;
                }
            }

            BufferedImage tmp = new BufferedImage(w, h, type);
            observer.imageUpdate(tmp, 0, 0, 0, w, h);
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, observer);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }
}
