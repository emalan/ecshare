package com.madalla.webapp.cms;

public interface IFileUploadStatus {

	boolean isUploading();

	/**
	 * Set when the upload thread starts, and reset when the upload ends or
	 * fails.
	 */
	void setIsUploading(boolean uploading);

	boolean isUploadComplete();

	/**
	 * Set when the upload thread succeeds, and reset when the upload page is
	 * reloaded.
	 */
	void setUploadComplete(boolean uploadComplete);
}
