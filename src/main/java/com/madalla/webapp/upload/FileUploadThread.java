package com.madalla.webapp.upload;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.form.upload.FileUpload;

public class FileUploadThread  extends Thread{
	
	private static final Log log = LogFactory.getLog(FileUploadThread.class);
	
	private final IFileUploadInfo uploadInfo;
	private final IFileUploadProcess process;
	private final String id;
	private final FileUploadGroup group;
	private transient final FileUpload fileUpload;

	public FileUploadThread(IFileUploadInfo uploadInfo, FileUpload fileUpload, IFileUploadProcess process, final String id) {
		this(uploadInfo, fileUpload, process, id, null);
	}
	public FileUploadThread(IFileUploadInfo uploadInfo,FileUpload fileUpload, IFileUploadProcess process, final String id, FileUploadGroup group) {
		this.uploadInfo = uploadInfo;
		this.process = process;
		this.id = id;
		this.group = group;
		this.fileUpload = fileUpload;
	}

	public void run() {
		
		try {
			
			String fileName = fileUpload.getClientFileName();
			
			//Check for existing process
			IFileUploadStatus status = uploadInfo.getFileUploadStatus(id);
			if (status != null && status.isUploading()){
				log.warn("Cannot upload submitted file. There is an existing process with the same name that is uploading. Name: " + fileName);
				return;
			}
			
			log.debug("Start processing: " + fileName);
			
			FileUploadStatus uploadStatus = new FileUploadStatus();
			if (group == null){
				uploadInfo.setFileUploadStatus(id, uploadStatus);
			} else {
				uploadInfo.setFileUploadStatus(id, group, uploadStatus);
			}
			
        	InputStream inputStream = fileUpload.getInputStream();
        	if (inputStream == null){
        		log.error("file upload - Input resource invalid.");
        	} else {
        		process.execute(inputStream, fileName);
        	}
        	
        	// Sleep to simulate time-consuming work
//			try {
//				Thread.sleep(20000);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}

			uploadStatus.uploading = false;
			
			log.debug("Done processing: " + fileName);
		} catch (IOException e) {
		//	session.error(e.getMessage());
		} finally {
			fileUpload.closeStreams();

		}
	}

}
