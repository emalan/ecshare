package com.madalla.webapp.upload;

public interface IFileUploadStatus {

	boolean isUploading();

	/**
	 * Set when the upload thread starts, and reset when the upload ends or
	 * fails.
	 */
	void setIsUploading(boolean uploading);

	
}
