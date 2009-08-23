package com.madalla.webapp.upload;


public interface IFileUploadInfo {
	
	IFileUploadStatus getFileUploadStatus(String id);
	
	void setFileUploadStatus(String id, IFileUploadStatus status);
	
	void setFileUploadComplete(String id);

}
