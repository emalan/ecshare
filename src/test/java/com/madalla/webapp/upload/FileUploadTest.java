package com.madalla.webapp.upload;

import java.util.List;

import junit.framework.TestCase;

public class FileUploadTest extends TestCase {

	public void testFileUploadStore() {

		FileUploadStore store = new FileUploadStore();

		// get status when there is none
		{
			IFileUploadStatus status = store.getFileUploadStatus("none");
			assertNull(status);

			List<String> list = store.getFileUploadStatus(new FileUploadGroup(""));
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
			FileUploadGroup group = new FileUploadGroup("group1");
			store.setFileUploadStatus(upload, group, new FileUploadStatus());
			IFileUploadStatus status = store.getFileUploadStatus(upload);
			assertNotNull(status);
			assertTrue(status.isUploading());

			List<String> list = store.getFileUploadStatus(group);
			assertTrue(list.size() == 1);

			list = store.getFileUploadStatus(new FileUploadGroup("none"));
			assertTrue(list.isEmpty());

			store.setFileUploadStatus("upload3", group, new FileUploadStatus());
			list = store.getFileUploadStatus(group);
			assertTrue(list.size() == 2);

			store.setFileUploadStatus("upload3", group, new FileUploadStatus());
			list = store.getFileUploadStatus(group);
			assertTrue(list.size() == 2);

			store.setFileUploadComplete(upload);
			status = store.getFileUploadStatus(upload);
			assertNull(status);
		}
		// get
	}
}
