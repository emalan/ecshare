package com.madalla.webapp.upload;

import java.io.Serializable;

public class FileUploadStatus implements IFileUploadStatus, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public boolean uploading = true;


	public boolean isUploading() {
		return uploading;
	}



}
