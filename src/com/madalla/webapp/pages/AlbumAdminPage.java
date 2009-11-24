package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureWebPage;
import com.madalla.webapp.panel.Panels;

public class AlbumAdminPage extends AdminPage implements ISecureWebPage {
	private static final long serialVersionUID = -5965988030475185795L;

	public AlbumAdminPage(final PageParameters params){
		super();
		add(Panels.albumAdminPanel("adminPanel", params));
	}
	
}
