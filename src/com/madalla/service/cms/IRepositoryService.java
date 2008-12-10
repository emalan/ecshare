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
    String getContentData(Page page, String id);
    String getContentData(Page page, String id, Locale locale);
    void saveContent(Content content);
    Content getContent(final String path);
    void pasteContent(String path, Content content);
    
    //Blog
    AbstractBlog getBlog(final String blogName);
    BlogEntry getNewBlogEntry(AbstractBlog blog, String title, DateTime date);
    BlogEntry getBlogEntry(final String path);
    void saveBlogEntry(BlogEntry blogEntry);
    List<AbstractBlogEntry> getBlogEntries(AbstractBlog blog);
    
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
