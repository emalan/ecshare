package com.madalla.webapp.blog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.service.blog.IBlogServiceProvider;
import com.madalla.service.cms.IContentServiceProvider;

public class BlogHomePanel extends Panel{

	private static final long serialVersionUID = 1548972105193261539L;
	private static Log log = LogFactory.getLog(BlogHomePanel.class);

	public BlogHomePanel(String id, String blog, Class<? extends Page> returnPage) {
		super(id);
		
		//Instanciation checks
		if (!(getApplication() instanceof IBlogServiceProvider) ||
				!(getApplication() instanceof IContentServiceProvider)){
			log.error("BlogHomePanel instanciation check failed on instanceof check.");
			throw new WicketRuntimeException("BlogHomePanel needs your wicket Application class to extend " +
					"the abstract class CmsBlogApplication.");
		}

		BlogDisplayPanel displayPanel = new BlogDisplayPanel("blogDisplayPanel", blog, returnPage);
		displayPanel.setOutputMarkupId(true);
		add(displayPanel);
		add(new BlogArchivePanel("blogExplorerPanel", blog, displayPanel));
		
		
	}
	

}
