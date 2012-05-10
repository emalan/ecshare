package com.madalla.webapp.admin.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;

import com.madalla.webapp.admin.AbstractAdminPage;
import com.madalla.webapp.user.UserProfilePanel;

/**
 * General Admin Page - General entry point to Admin Panels for applications.
 *
 * @author Eugene Malan
 *
 */
@AuthorizeInstantiation("USER")
public class MainAdminPage extends AbstractAdminPage {

	private static final long serialVersionUID = 1L;

    public MainAdminPage(){
		super();
		add(new UserProfilePanel(ID));
	}


}
