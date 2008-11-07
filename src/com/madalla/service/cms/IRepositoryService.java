package com.madalla.service.cms;

import java.util.List;
import java.util.Locale;

import javax.swing.tree.TreeModel;

import org.joda.time.DateTime;

import com.madalla.service.cms.jcr.Content;

public interface IRepositoryService {

	//package methods
    String getLocaleId(String id, Locale locale);

    //public
	boolean isDeletableNode(final String path);
	boolean isContentNode(final String path);
    boolean isBlogNode(final String path);
    boolean isImageNode(final String path);
    boolean isContentPasteNode(final String path);
    String getContentData(String nodeName, String id);
    String getContentData(final String nodeName, final String id, Locale locale);
    void pasteContent(final String path, final Content content);
    AbstractBlogEntry getBlogEntry(final String uuid);
    AbstractBlogEntry getNewBlogEntry(String blog, String title, DateTime date);
    void deleteNode(final String path);
    Content getContent(final String path);
    List<AbstractBlogEntry> getBlogEntries(final String blog);
    AbstractImageData getImageData(final String path) ;
    List<AbstractImageData> getAlbumImages(final String album);
    List<AbstractImageData> getAlbumOriginalImages();
    TreeModel getAlbumImagesAsTree(final String album);

}
