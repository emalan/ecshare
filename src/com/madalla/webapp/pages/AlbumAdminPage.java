package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.Panels;

public class AlbumAdminPage extends AdminPage {
	private static final long serialVersionUID = -5965988030475185795L;

	public AlbumAdminPage(final PageParameters params){
		add(Panels.albumAdminPanel("albumAdminPanel", params));
	}
	
}
