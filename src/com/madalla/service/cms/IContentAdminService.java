package com.madalla.service.cms;

import java.io.File;

import javax.swing.tree.TreeModel;

public interface IContentAdminService {

	TreeModel getSiteContent();
	TreeModel getRepositoryContent();
	void backupContentRoot();
	void backupContentSite();
	File[] getBackupFileList();
	File[] getApplicationBackupFileList();
	void restoreContentSite(File backupFile);
	void restoreContentApplication(final File backupFile);
}
