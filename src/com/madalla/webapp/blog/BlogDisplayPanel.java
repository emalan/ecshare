package com.madalla.webapp.blog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;

import com.madalla.service.blog.BlogEntry;

public class BlogDisplayPanel extends AbstractBlogDisplayPanel{
	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(this.getClass());
	
	public BlogDisplayPanel(String id, final PageParameters parameters, Class blogEntryPage, boolean adminMode) {
		super(id, blogEntryPage, adminMode);
		
		int blogEntryId = parameters.getInt(BLOG_ENTRY_ID);
        log.debug("Constructing Blog Entry. id="+blogEntryId);
        BlogEntry blogEntry = getBlogService().getBlogEntry(blogEntryId);
		populateBlogEntryDisplay(this, blogEntry, blogEntryPage, adminMode);
		add(new Label("text", blogEntry.getText()).setEscapeModelStrings(false));
	}

	
}
