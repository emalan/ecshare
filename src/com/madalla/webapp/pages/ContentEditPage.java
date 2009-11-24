package com.madalla.webapp.pages;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureWebPage;
import com.madalla.webapp.cms.editor.ContentEntryPanel;

public class ContentEditPage extends AdminPage implements ISecureWebPage {
	private static final long serialVersionUID = -264932041802936603L;

	public ContentEditPage(String contentId, String nodeName){
		add(new ContentEntryPanel("adminPanel", nodeName, contentId));
		
	}
}
