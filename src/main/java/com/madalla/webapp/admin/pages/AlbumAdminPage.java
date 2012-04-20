package com.madalla.webapp.admin.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.util.string.StringValue;

import com.madalla.webapp.admin.AbstractAdminPage;
import com.madalla.webapp.admin.image.AlbumAdminPanel;

@AuthorizeInstantiation("ADMIN")
public class AlbumAdminPage extends AbstractAdminPage  {
	private static final long serialVersionUID = -5965988030475185795L;

	public AlbumAdminPage(PageParameters parameters){
		super(parameters);
		StringValue param = parameters.get(0);
		add(new AlbumAdminPanel("adminPanel", param.toString()));
	}

}
