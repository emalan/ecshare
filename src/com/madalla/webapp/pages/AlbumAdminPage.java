package com.madalla.webapp.pages;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureWebPage;
import com.madalla.webapp.images.admin.AlbumAdminPanel;

public class AlbumAdminPage extends AdminPage implements ISecureWebPage {
	private static final long serialVersionUID = -5965988030475185795L;

	public AlbumAdminPage(String albumName){
		super();
		add(new AlbumAdminPanel("adminPanel", albumName));
	}
	
}
