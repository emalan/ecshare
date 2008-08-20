package com.madalla.service.cms;

import java.io.File;
import java.util.List;

import javax.swing.tree.TreeModel;

public interface IContentAdminService {

	TreeModel getSiteContent();
	TreeModel getRepositoryContent();
	String backupContentRoot();
	String backupContentSite();
	List<File> getBackupFileList();
	List<File> getApplicationBackupFileList();
	void restoreContentSite(File backupFile);
	void restoreContentApplication(final File backupFile);
	Boolean isRollbackSiteAvailable();
	void rollbackSiteRestore();
	Boolean isRollbackApplicationAvailable();
	void rollbackApplicationRestore();
	
}
