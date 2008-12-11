package com.madalla.service.cms;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import javax.swing.tree.TreeModel;

import org.joda.time.DateTime;

import com.madalla.service.cms.ocm.blog.BlogEntry;
import com.madalla.service.cms.ocm.image.Album;
import com.madalla.service.cms.ocm.image.Image;
import com.madalla.service.cms.ocm.page.Content;
import com.madalla.service.cms.ocm.page.Page;

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
    Page getPage(final String name);
    String getContentText(Page page, String id);
    String getContentText(Page page, String id, Locale locale);
    
    Content getContent(final Page parent, final String name, final Locale locale);
    Content getContent(final Page parent, final String name);
    Content getContent(final String path);
    
    void saveContent(Content content);
    void pasteContent(String path, Content content);
    
    //Blog
    IBlogData getBlog(final String blogName);
    BlogEntry getNewBlogEntry(IBlogData blog, String title, DateTime date);
    BlogEntry getBlogEntry(final String path);
    void saveBlogEntry(BlogEntry blogEntry);
    List<AbstractBlogEntry> getBlogEntries(IBlogData blog);
    
    //Image
    Album getOriginalsAlbum();
    Album getAlbum(final String name);
    String createImage(Album album, String name, InputStream inputStream);
    void addImageToAlbum(Album album, String imageId);
    Image getImage(final String path);
    List<Image> getAlbumImages(Album album);
    List<Image> getAlbumOriginalImages();
    TreeModel getAlbumImagesAsTree(final Album album);

}
