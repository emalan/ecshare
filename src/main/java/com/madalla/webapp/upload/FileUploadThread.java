package com.madalla.webapp.upload;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread to perform the file upload.
 * 
 * @author Eugene Malan
 *
 */
public class FileUploadThread  extends Thread{

	private static final Logger log = LoggerFactory.getLogger(FileUploadThread.class);

	private final IFileUploadInfo uploadInfo;
	private final IFileUploadProcess process;
	private final String id;
	private final FileUploadGroup group;
	private transient final InputStream inputStream;

	/**
	 * @param uploadInfo
	 * @param fileUpload
	 * @param process
	 * @param id
	 */
	public FileUploadThread(IFileUploadInfo uploadInfo, InputStream inputStream, IFileUploadProcess process, final String id) {
		this(uploadInfo, inputStream, process, id, null);
	}
	
	/**
	 * Main constructor.
	 * 
	 * @param uploadInfo - This object will be informed of the status of the upload.
	 * @param fileUpload - The details of the file to be processed
	 * @param process - Actual process that will be executed to run the upload.
	 * @param id - The id to be assigned to the uploaded stream.
	 * @param group - 
	 */
	public FileUploadThread(IFileUploadInfo uploadInfo, InputStream inputStream, IFileUploadProcess process, final String id, FileUploadGroup group) {
		this.uploadInfo = uploadInfo;
		this.process = process;
		this.id = id;
		this.group = group;
		this.inputStream = inputStream;
	}

	@Override
	public void run() {

		try {

			//Check for existing process
			IFileUploadStatus status = uploadInfo.getFileUploadStatus(id);
			if (status != null && status.isUploading()){
				log.warn("Cannot upload submitted file. There is an existing process with the same name that is uploading. Name: " + id);
				return;
			}

			log.debug("Start processing: " + id);

			FileUploadStatus uploadStatus = new FileUploadStatus();
			if (group == null){
				uploadInfo.setFileUploadStatus(id, uploadStatus);
			} else {
				uploadInfo.setFileUploadStatus(id, group, uploadStatus);
			}

        	if (inputStream == null){
        		log.error("file upload - Input resource invalid.");
        	} else {
        		process.execute(inputStream, id);
        	}

        	// Sleep to simulate time-consuming work
			try {
				Thread.sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			uploadStatus.uploading = false;

			log.debug("Done processing: " + id);
			
		} finally {
			try {
                inputStream.close();
            } catch (IOException e) {
                // nothing to do here
            }

		}
	}

}
