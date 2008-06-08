package com.madalla.webapp.blog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;

import com.madalla.service.blog.BlogEntry;

public class BlogDisplayPanel extends AbstractBlogDisplayPanel{
	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(this.getClass());
	
	public BlogDisplayPanel(String id, final PageParameters parameters, Class blogEntryPage, 
			Class blogMainPage, Class blogArchivePage, boolean adminMode) {
		super(id);
		
		String blogEntryId = parameters.getString(BLOG_ENTRY_ID);
		add(new BookmarkablePageLink("main",blogMainPage));
		add(new BookmarkablePageLink("archive",blogArchivePage));
		
		log.debug("Constructing Blog Entry. id="+blogEntryId);
        BlogEntry blogEntry = getBlogService().getBlogEntry(blogEntryId);
		populateBlogEntryDisplay(this, blogEntry, blogEntryPage, adminMode);
		add(new Label("text", blogEntry.getText()).setEscapeModelStrings(false));
	}

	
}
