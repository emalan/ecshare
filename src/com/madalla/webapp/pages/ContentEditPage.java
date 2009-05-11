package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureWebPage;
import com.madalla.webapp.Panels;

public class ContentEditPage extends AdminPage implements ISecureWebPage {
	private static final long serialVersionUID = -264932041802936603L;

	public ContentEditPage(final PageParameters parameters){
		add(Panels.contentEntryPanel("adminPanel",parameters ));
	}
}
