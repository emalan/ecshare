package com.madalla.webapp.upload;

import java.io.Serializable;

public class FileUploadStatus implements IFileUploadStatus, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private boolean uploading;


	public boolean isUploading() {
		return uploading;
	}

	public void setIsUploading(boolean uploading) {
		this.uploading = uploading;
	}

}
