package com.madalla.webapp.upload;

import java.util.List;


public interface IFileUploadInfo {

	IFileUploadStatus getFileUploadStatus(String id);

	void setFileUploadStatus(String id, IFileUploadStatus status);

	void setFileUploadComplete(String id);

	void setFileUploadStatus(String id, FileUploadGroup group, IFileUploadStatus status) ;

	void setGroupUploadComplete(FileUploadGroup group);

	List<String> getFileUploadStatus(FileUploadGroup group);

}
