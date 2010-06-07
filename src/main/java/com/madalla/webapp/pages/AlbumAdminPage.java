package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;

import com.madalla.webapp.admin.AdminPage;
import com.madalla.webapp.images.admin.AlbumAdminPanel;

@AuthorizeInstantiation("ADMIN")
public class AlbumAdminPage extends AdminPage  {
	private static final long serialVersionUID = -5965988030475185795L;

	public AlbumAdminPage(PageParameters parameters){
		super(parameters);
		add(new AlbumAdminPanel("adminPanel", parameters.getString("0")));
	}
	
}
