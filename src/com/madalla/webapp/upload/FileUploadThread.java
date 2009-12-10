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
	private final FileUpload fileUpload;

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
		FileUploadStatus uploadStatus = new FileUploadStatus();
		if (group == null){
			uploadInfo.setFileUploadStatus(id, uploadStatus);
		} else {
			uploadInfo.setFileUploadStatus(id, group, uploadStatus);
		}
		
		try {
			String fileName = fileUpload.getClientFileName();
			log.debug("Start processing: " + fileName);
			
        	InputStream inputStream = fileUpload.getInputStream();
        	if (inputStream == null){
        		log.error("file upload - Input resource invalid.");
        	} else {
        		process.execute(inputStream, fileName);
        	}

			// Sleep to simulate time-consuming work
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log.debug("Done processing: " + fileName);
			uploadStatus.uploading = false;
		} catch (IOException e) {
		//	session.error(e.getMessage());
		} finally {
			//session.setFileUploadComplete(data.getId());

		}
	}

}
