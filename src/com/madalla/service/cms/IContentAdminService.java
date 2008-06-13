package com.madalla.service.cms;

import java.io.File;

import javax.swing.tree.TreeModel;

public interface IContentAdminService {

	public TreeModel getSiteContent();
	public void backupContentRoot();
	public void backupContentSite();
	public File[] getBackupFileList();
	public void restoreContentSite(File backupFile) ;
}
