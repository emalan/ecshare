/**
 * Provides UI functionality for Photos 
 * <p>
 * TODO Wicket Panel for uploading Photos - use example at Wicket site.
 * TODO Create Service and Utility packages
 * TODO Switch JNDI Respository to use MySQL so we can see what is going on with binary data 
 * <p>
 * Here is the plan...
 * This package saves the photos to File and informs a listener. 
 * Seperate process reads images (java.io), processes images(java.awt.image) ie. thumbnails etc,
 * saves it to Repository.
 * Also need to test how repository manages binary files and incorporate that into backup plan.
 *  
 * @author Eugene Malan
 */
package com.madalla.webapp.images;