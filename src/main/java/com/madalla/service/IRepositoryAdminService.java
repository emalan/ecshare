package com.madalla.service;

import java.io.File;
import java.util.List;

import javax.swing.tree.DefaultTreeModel;

import com.madalla.cms.jcr.NodeDisplay;

/**
 * Administrative functions for the Content Repository including backup.
 * 
 * @author Eugene Malan
 *
 */
public interface IRepositoryAdminService {

	NodeDisplay getNodeDisplay(final String path);
	
	/**
	 * All Content Data for a site/application
	 * @return CMS data as a Tree for display
	 */
	DefaultTreeModel getSiteContent();

	/**
	 * All Content Data in repository
	 * @return CMS data as a Tree for display
	 */
	DefaultTreeModel getRepositoryContent();

	/**
	 * Will backup all Data.
	 * @return - the name of the file used for backup
	 */
	String backupContentRoot();

	/**
	 * Backs up data for this site
	 * @return - the name of the file used for backup
	 */
	String backupContentSite();

	/**
	 * @return - list of the backup files for this site
	 */
	List<BackupFile> getBackupFileList();

	/**
	 * @return - list of backup files that have full CMS backup
	 */
	List<BackupFile> getApplicationBackupFileList();

	/**
	 * Restore the Site data from the supplied file. This is risky
	 * and should be verified once done. There is functionality to
	 * rollback the last restore.
	 * 
	 * @param backupFile - file to use for restore.
	 */
	void restoreContentSite(File backupFile);

	/**
	 * Restore the whole CMS from the supplied file. This is risky
	 * and should be verified once done. There is functionality to
	 * rollback the last restore.
	 * 
	 * @param backupFile - file to use for restore
	 */
	void restoreContentApplication(final File backupFile);

	/**
	 * When restoring the CMS, the exising data is moved and is 
	 * available to rollback the data to before the Restore.
	 * @return - true if there is a rollback available.
	 */
	Boolean isRollbackSiteAvailable();

	/**
	 * When restoring the CMS, the exising data is moved and is
	 * available to be re-instated. This will rollback the last 
	 * restore of the Site data.
	 * 
	 * The restore is the riskiest thing we can do, so care should be 
	 * taken to verify a restore once it it done. You can only rollback
	 * one restore.
	 */
	void rollbackSiteRestore();

	/**
	 * When restoring the CMS, the exising data is moved and is 
	 * available to rollback the data to before the Restore.
	 * @return - true if there is a rollback available.
	 */
	Boolean isRollbackApplicationAvailable();
	
	/**
	 * When restoring the CMS, the exising data is moved and is
	 * available to be re-instated. This will rollback the last 
	 * restore of the CMS data.
	 * 
	 * The restore is the riskiest thing we can do, so care should be 
	 * taken to verify a restore once it it done. You can only rollback
	 * one restore.
	 */
	void rollbackApplicationRestore();
}
