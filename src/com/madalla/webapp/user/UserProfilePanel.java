package com.madalla.webapp.user;

import org.apache.wicket.Page;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;

public class UserProfilePanel extends Panel{

	private static final long serialVersionUID = 9027719184960390850L;

	public UserProfilePanel(String id, Class<? extends Page> returnPage){
		
		super(id);
		
		add(HeaderContributor.forJavaScript(Scriptaculous.PROTOTYPE));
		
		add(new PageLink("returnLink", returnPage));
		
	}
}
