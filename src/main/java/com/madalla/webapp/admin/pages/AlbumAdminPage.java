package com.madalla.webapp.admin.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;

import com.madalla.webapp.admin.AbstractAdminPage;
import com.madalla.webapp.admin.image.AlbumAdminPanel;

@AuthorizeInstantiation("ADMIN")
public class AlbumAdminPage extends AbstractAdminPage  {
	private static final long serialVersionUID = -5965988030475185795L;

	public AlbumAdminPage(PageParameters parameters){
		super(parameters);
		add(new AlbumAdminPanel("adminPanel", parameters.getString("0")));
	}
	
}
