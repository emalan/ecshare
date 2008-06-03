package com.madalla.service.cms;

import javax.swing.tree.TreeModel;

public interface IContentAdminService {

	public TreeModel getSiteContent();
	public void backupContentRoot();
	public void backupContentApps();
	public void backupContentSite();
}
