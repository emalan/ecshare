package com.madalla.service.cms;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import javax.swing.tree.TreeModel;

import org.joda.time.DateTime;

import com.madalla.service.cms.ocm.image.Album;
import com.madalla.service.cms.ocm.image.Image;

public interface IRepositoryService {

	// package methods
	String getLocaleId(String id, Locale locale);

	// type checks
	boolean isDeletableNode(final String id);

	boolean isContentNode(final String id);

	boolean isBlogNode(final String id);

	boolean isImageNode(final String id);

	boolean isContentPasteNode(final String id);

	// Generic
	void deleteNode(final String id);

	// Content
	PageData getPage(final String name);

	String getContentText(IPageData page, String id);

	String getContentText(IPageData page, String id, Locale locale);

	ContentData getContent(final IPageData parent, final String name,
			final Locale locale);

	ContentData getContent(final IPageData parent, final String name);

	ContentData getContent(final String id);

	void saveContent(IContentData content);

	void pasteContent(String id, IContentData content);

	// Blog
	IBlogData getBlog(final String blogName);

	IBlogEntryData getNewBlogEntry(IBlogData blog, String title, DateTime date);

	IBlogEntryData getBlogEntry(final String id);

	void saveBlogEntry(IBlogEntryData blogEntry);

	List<BlogEntryData> getBlogEntries(IBlogData blog);

	// Image
	Album getOriginalsAlbum();

	Album getAlbum(final String name);

	String createImage(Album album, String name, InputStream inputStream);

	void addImageToAlbum(Album album, String name);

	Image getImage(final String id);

	List<Image> getAlbumImages(Album album);

	List<Image> getAlbumOriginalImages();

	TreeModel getAlbumImagesAsTree(final Album album);

}
