package com.madalla.webapp.upload;

import java.util.Collection;

import junit.framework.TestCase;

public class FileUploadTest extends TestCase {

	public void testFileUploadStore() {

		FileUploadStore store = new FileUploadStore();

		// get status when there is none
		{
			IFileUploadStatus status = store.getFileUploadStatus("none");
			assertNull(status);

			Collection<IFileUploadStatus> list = store.getFileUploadStatus(new FileUploadGroup(""));
			assertTrue(list.isEmpty());

			store.setFileUploadComplete("none");
			store.setGroupUploadComplete(new FileUploadGroup("none"));
		}

		// start an upload
		{
			String upload = "upload1";
			store.setFileUploadStatus(upload, new FileUploadStatus());
			IFileUploadStatus status = store.getFileUploadStatus(upload);
			assertNotNull(status);
			assertTrue(status.isUploading());
			
			store.setFileUploadComplete(upload);
			status = store.getFileUploadStatus(upload);
			assertNull(status);
		}

		// start a group upload
		{
			String upload = "upload2";
			String group = "group1";
			store.setFileUploadStatus(upload, new FileUploadGroup(group), new FileUploadStatus());
			IFileUploadStatus status = store.getFileUploadStatus(upload);
			assertNotNull(status);
			assertTrue(status.isUploading());
			
			Collection<IFileUploadStatus> list = store.getFileUploadStatus(new FileUploadGroup(group));
			assertTrue(list.size() == 1);
			
			list = store.getFileUploadStatus(new FileUploadGroup("none"));
			assertTrue(list.isEmpty());
			
			store.setFileUploadStatus("upload3", new FileUploadGroup(group), new FileUploadStatus());
			list = store.getFileUploadStatus(new FileUploadGroup(group));
			assertTrue(list.size() == 2);
			
			store.setFileUploadComplete(upload);
			status = store.getFileUploadStatus(upload);
			assertNull(status);
		}
		// get
	}
}
