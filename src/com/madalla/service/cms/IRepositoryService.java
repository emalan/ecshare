package com.madalla.service.cms;

import com.madalla.service.cms.jcr.BlogEntry;
import com.madalla.service.cms.jcr.Content;
import com.madalla.service.cms.jcr.ImageData;

import javax.swing.tree.TreeModel;
import java.util.List;
import java.util.Locale;

public interface IRepositoryService {

	//package methods
    String getLocaleId(String id, Locale locale);

    //public
	boolean isDeletableNode(final String path);
	boolean isContentNode(final String path);
    boolean isBlogNode(final String path);
    boolean isContentPasteNode(final String path);
    String getContentData(String nodeName, String id);
    String getContentData(final String nodeName, final String id, Locale locale);
    void pasteContent(final String path, final Content content);
    BlogEntry getBlogEntry(final String uuid);
    void deleteNode(final String path);
    Content getContent(final String path);
    List<BlogEntry> getBlogEntries(final String blog);
    ImageData getImageData(final String path) ;
    List<ImageData> getAlbumImages(final String album);
    List<ImageData> getAlbumOriginalImages();
    TreeModel getAlbumImagesAsTree(final String album);

}
