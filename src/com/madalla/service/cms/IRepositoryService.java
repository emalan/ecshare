package com.madalla.service.cms;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

import javax.swing.tree.TreeModel;

import org.joda.time.DateTime;

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

	void saveContent(ContentData content);

	void pasteContent(String id, ContentData content);

	// Blog
	BlogData getBlog(final String blogName);

	BlogEntryData getNewBlogEntry(IBlogData blog, String title, DateTime date);

	BlogEntryData getBlogEntry(final String id);

	void saveBlogEntry(BlogEntryData blogEntry);

	List<BlogEntryData> getBlogEntries(BlogData blog);

	// Image
	AlbumData getOriginalsAlbum();

	AlbumData getAlbum(final String name);

	String createImage(IAlbumData album, String name, InputStream inputStream);

	void addImageToAlbum(IAlbumData album, String name);

	IImageData getImage(final String id);

	List<ImageData> getAlbumImages(AlbumData album);

	List<ImageData> getAlbumOriginalImages();

	TreeModel getAlbumImagesAsTree(final AlbumData album);

}
