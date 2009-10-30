package com.madalla.webapp.pages;

import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.ISecureWebPage;
import com.madalla.webapp.panel.Panels;

/**
 * @author Eugene Malan
 */
public class TranslatePage extends AdminPage implements ISecureWebPage {

	public TranslatePage(final PageParameters parameters) {
		super(parameters);
		add(Panels.translatePanel("adminPanel", parameters));
	}
}

