package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.Panels;

public class ContentEditPage extends AdminPage {

	private static final long serialVersionUID = -264932041802936603L;

	public ContentEditPage(final PageParameters parameters){
		super();
		add(Panels.contentEntryPanel("contentEntryPanel",parameters ));
	}
}
