package com.madalla.webapp.admin.pages;

import org.apache.wicket.PageParameters;
import org.apache.wicket.authorization.strategies.role.annotations.AuthorizeInstantiation;

import com.madalla.webapp.admin.AbstractAdminPage;
import com.madalla.webapp.cms.editor.ContentEntryPanel;

@AuthorizeInstantiation("USER")
public class ContentEditPage extends AbstractAdminPage {
	private static final long serialVersionUID = -264932041802936603L;

	public ContentEditPage(PageParameters parameters){
		super(parameters);
		String contentId = PageUtils.getPageParameter("0", parameters, "ContentEditPage");
		String nodeName = PageUtils.getPageParameter("1", parameters, "ContentEditPage");
		add(new ContentEntryPanel("adminPanel", nodeName, contentId));
	}

}
