package com.madalla.webapp.blog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import com.madalla.service.blog.BlogEntry;
import com.madalla.service.blog.IBlogService;

public class BlogDisplaySummaryPanel extends AbstractBlogDisplayPanel {
	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(this.getClass());
	private int displayCount = 5;
	
	public BlogDisplaySummaryPanel(String id, final Class blogEntryPage, final Class blogDisplayPage, final boolean adminMode) {
		super(id, blogEntryPage, adminMode);
        
		//new Blog link
        add(new BookmarkablePageLink("CreateNew",blogEntryPage, new PageParameters(BLOG_ENTRY_ID+"=0")){
            private static final long serialVersionUID = 1L;

            protected final void onBeforeRender(){
                if (adminMode){
                    setVisible(true);
                } else {
                    setVisible(false);
                }
                super.onBeforeRender();
            }
        });
        
        //List existing Blogs
        log.debug("construtor - retrieving blog entries from service.");
        IBlogService service = getBlogService();
        List commentList = service.getBlogEntries();
        log.debug("construtor - retrieved blog entries. count="+commentList.size());
        ListView listView = new ListView("comments", commentList) {
            public void populateItem(final ListItem listItem) {
                final BlogEntry blogEntry = (BlogEntry) listItem.getModelObject();
                populateBlogEntryDisplay(listItem, blogEntry, blogEntryPage, adminMode);
                listItem.add(new Label("text", blogEntry.getSummary()).setEscapeModelStrings(false));
                
                //Link to Blog Display Page
                Map params = new HashMap();
                params.put(BLOG_ENTRY_ID,new Integer(blogEntry.getId()));
                listItem.add(new BookmarkablePageLink("DisplayBlog",blogDisplayPage,new PageParameters(BLOG_ENTRY_ID+"="+blogEntry.getId())));
            }
        };
        listView.setViewSize(displayCount);
        add(listView);

	}

	

}
