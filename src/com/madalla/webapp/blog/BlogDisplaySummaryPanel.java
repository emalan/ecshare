package com.madalla.webapp.blog;

import java.util.List;

import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import com.madalla.service.blog.BlogEntry;
import com.madalla.service.blog.IBlogService;

public class BlogDisplaySummaryPanel extends BlogDisplayPanel {
	private static final long serialVersionUID = 1L;
	private int displayCount = 5;
	
	public BlogDisplaySummaryPanel(String id, final Class blogEntryPage, final boolean adminMode, IBlogService service) {
		super(id, blogEntryPage, adminMode, service);
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
        List commentList = service.getBlogEntries();
        ListView listView = new ListView("comments", commentList) {
            public void populateItem(final ListItem listItem) {
                final BlogEntry blogEntry = (BlogEntry) listItem.getModelObject();
                add(new BlogDisplayComponent("blogDisplay",blogEntry, blogEntryPage, adminMode));
            }

        };
        listView.setViewSize(displayCount);
        add(listView);


	}

	

}
