package com.madalla.webapp.upload;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FileUploadThread  extends Thread{
	
	private static final Log log = LogFactory.getLog(FileUploadThread.class);
	
	private final IFileUploadInfo uploadInfo;
	private final IFileUploadProcess process;
	private final String id;

	public FileUploadThread(IFileUploadInfo uploadInfo, IFileUploadProcess process, final String id) {
		this.uploadInfo = uploadInfo;
		this.process = process;
		this.id = id;
	}

	public void run() {
		FileUploadStatus uploadStatus = new FileUploadStatus();
		uploadInfo.setFileUploadStatus(id, uploadStatus);
		
		try {
			log.debug("Start processing...");
			process.execute();

			// Sleep to simulate time-consuming work
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log.debug("Done processing...");
			uploadStatus.uploading = false;
		//} catch (InterruptedException e) {
		//	session.error(e.getMessage());
		} finally {
			//session.setFileUploadComplete(data.getId());

		}
	}

}
