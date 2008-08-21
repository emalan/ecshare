package com.madalla.webapp.blog;

import org.apache.wicket.markup.html.panel.Panel;

public class BlogHomePanel extends Panel{

	private static final long serialVersionUID = 1548972105193261539L;
	private static final String BLOG = "mainBlog";

	public BlogHomePanel(String id, String blog) {
		super(id);
		BlogDisplayPanel displayPanel = new BlogDisplayPanel("blogDisplayPanel", BLOG);
		displayPanel.setOutputMarkupId(true);
		add(displayPanel);
		add(new BlogArchivePanel("blogExplorerPanel", BLOG, displayPanel));
		
		
	}

}
