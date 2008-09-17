package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebPage;

import com.madalla.webapp.images.admin.AlbumAdminPanel;

//TODO secure by extending AdminPage
public class AlbumAdminPage extends WebPage {
	private static final long serialVersionUID = -5965988030475185795L;

	public AlbumAdminPage(final PageParameters params){
		super(params);
		add(new AlbumAdminPanel("albumAdminPanel", params ));
	}
	
}
