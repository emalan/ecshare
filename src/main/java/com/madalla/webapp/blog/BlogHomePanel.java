package com.madalla.webapp.blog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.WicketRuntimeException;

import com.madalla.bo.blog.BlogData;
import com.madalla.service.IDataServiceProvider;
import com.madalla.webapp.CmsPanel;

public class BlogHomePanel extends CmsPanel{

	private static final long serialVersionUID = 1548972105193261539L;
	private static Log log = LogFactory.getLog(BlogHomePanel.class);
	
	
	public BlogHomePanel(String id, String blogName, String blogEntryId){
		super(id);
		
		//Instantiation checks
		if (!(getApplication() instanceof IDataServiceProvider)){
			log.error("BlogHomePanel instanciation check failed on instanceof check.");
			throw new WicketRuntimeException("BlogHomePanel needs your wicket Application class to extend " +
					"the abstract class CmsApplication.");
		}

		BlogData blog = getRepositoryService().getBlog(blogName);
		BlogDisplayPanel displayPanel;
		if (blogEntryId != null){
			displayPanel = new BlogDisplayPanel("blogDisplayPanel", blog, blogEntryId);
		} else {
			displayPanel = new BlogDisplayPanel("blogDisplayPanel", blog);
		}
		displayPanel.setOutputMarkupId(true);
		add(displayPanel);
		add(new BlogArchivePanel("blogExplorerPanel", blog, displayPanel));
	}


}
