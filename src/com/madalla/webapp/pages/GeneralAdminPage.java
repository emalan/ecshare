package com.madalla.webapp.pages;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureWebPage;
import com.madalla.webapp.user.UserProfilePanel;

/**
 * General Admin Page - Common entry point to Admin Panels
 * 
 * Panels are switched using AdminPanelLink
 * 
 * @author Eugene Malan
 *
 */
public class GeneralAdminPage extends AdminPage implements ISecureWebPage  {
	protected static final String ID = "adminPanel";
	
	public GeneralAdminPage(){
		add(new UserProfilePanel(ID));
	}
	

}
