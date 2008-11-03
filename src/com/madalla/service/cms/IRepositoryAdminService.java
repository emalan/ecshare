package com.madalla.service.cms;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.swing.tree.TreeModel;

public interface IRepositoryAdminService {

	TreeModel getSiteContent();
	TreeModel getRepositoryContent();
	String backupContentRoot();
	String backupContentSite();
	List<BackupFile> getBackupFileList();
	List<BackupFile> getApplicationBackupFileList();
	void restoreContentSite(File backupFile);
	void restoreContentApplication(final File backupFile);
	Boolean isRollbackSiteAvailable();
	void rollbackSiteRestore();
	Boolean isRollbackApplicationAvailable();
	void rollbackApplicationRestore();
	
	String createOriginalImage(final String imageName, final InputStream fullImage);
	String addImageToAlbum(String album, String imageName);
	
}
