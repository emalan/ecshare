package com.madalla.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

/**
 * Graphics Utilities class that provide default application methods.
 * 
 * @author Eugene Malan
 *
 */
public class ImageUtilities {

	/**
	 * @param bufferedImage
	 * @param targetWidth
	 * @param targetHeight
	 * @return BufferedImage that has been scaled to the specified height and width.
	 */
	public static BufferedImage getScaledInstance(BufferedImage bufferedImage, int targetWidth, int targetHeight){
		return getScaledInstance(bufferedImage, targetWidth, targetHeight, 
				RenderingHints.VALUE_INTERPOLATION_BICUBIC, true, null);
	}

	/**
	 * @param bufferedImage
	 * @param targetWidth
	 * @param targetHeight
	 * @return BufferedImage that has been scaled down proportionately to within the specified height and width.
	 */
	public static BufferedImage getScaledDownProportionalInstance(BufferedImage bufferedImage, 
			int targetWidth, int targetHeight, ImageObserver imageObserver){
		double actualWidth = bufferedImage.getWidth();
		double actualHeight = bufferedImage.getHeight();
		double scaleFactor = (double)targetWidth / actualWidth;
		if (actualHeight * scaleFactor > targetHeight){
			scaleFactor = targetHeight / actualHeight;
		}
		if (scaleFactor >= 1){
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
	public static BufferedImage getScaledProportinalInstance(BufferedImage bufferedImage, 
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
            Graphics2D g2 = tmp.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, hint);
            g2.drawImage(ret, 0, 0, w, h, observer);
            g2.dispose();

            ret = tmp;
        } while (w != targetWidth || h != targetHeight);

        return ret;
    }
}
