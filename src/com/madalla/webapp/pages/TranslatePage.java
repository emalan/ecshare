package com.madalla.webapp.pages;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureWebPage;
import com.madalla.webapp.cms.editor.TranslatePanel;

/**
 * @author Eugene Malan
 */
public class TranslatePage extends AdminPage implements ISecureWebPage {

	public TranslatePage(String contentId, String nodeName) {
		add(new TranslatePanel("adminPanel", nodeName, contentId));
		
	}
}

