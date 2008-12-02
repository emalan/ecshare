package com.madalla.service.cms;

import java.util.List;
import java.util.Locale;

import javax.swing.tree.TreeModel;

import org.joda.time.DateTime;

import com.madalla.service.cms.jcr.Content;
import com.madalla.service.cms.ocm.blog.BlogEntry;

public interface IRepositoryService {

	//package methods
    String getLocaleId(String id, Locale locale);

    //type checks
	boolean isDeletableNode(final String path);
	boolean isContentNode(final String path);
    boolean isBlogNode(final String path);
    boolean isImageNode(final String path);
    boolean isContentPasteNode(final String path);
    
    //Generic
    void deleteNode(final String path);
    
    //Content
    String getContentData(String nodeName, String id);
    String getContentData(final String nodeName, final String id, Locale locale);
    void pasteContent(final String path, final Content content);
    Content getContent(final String path);
    
    //Blog
    AbstractBlog getBlog(final String blogName);
    BlogEntry getNewBlogEntry(AbstractBlog blog, String title, DateTime date);
    BlogEntry getBlogEntry(final String path);
    void saveBlogEntry(BlogEntry blogEntry);
    List<AbstractBlogEntry> getBlogEntries(AbstractBlog blog);
    @Deprecated
    AbstractBlogEntry getNewBlogEntry(String blog, String title, DateTime date);
    @Deprecated
    List<AbstractBlogEntry> getBlogEntries(final String blog);
    
    //Image
    AbstractImageData getImageData(final String path) ;
    List<AbstractImageData> getAlbumImages(final String album);
    List<AbstractImageData> getAlbumOriginalImages();
    TreeModel getAlbumImagesAsTree(final String album);

}
