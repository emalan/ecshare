package com.madalla.webapp.blog;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.PageParameters;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;

import com.madalla.service.blog.BlogEntry;
import com.madalla.service.blog.IBlogService;
import com.madalla.service.blog.IBlogServiceProvider;

public class BlogDisplayComponent extends MarkupContainer implements IBlogAware {
	private static final long serialVersionUID = 1L;

	public BlogDisplayComponent(String id, final BlogEntry blogEntry, final Class blogEntryPage,
			final boolean adminMode) {
		super(id);
		add(new DateLabel("date", new Model(blogEntry.getDate()),
				new StyleDateConverter("MS", true)));
		add(new Label("text", blogEntry.getSummary())
				.setEscapeModelStrings(false));

		//Edit link
		Map params = new HashMap();
		params.put(BLOG_ENTRY_ID, new Integer(blogEntry.getId()));
		add(new BookmarkablePageLink("EditBlog", blogEntryPage,
				new PageParameters(BLOG_ENTRY_ID + "=" + blogEntry.getId())) {
			private static final long serialVersionUID = 1L;

			protected final void onBeforeRender() {
				if (adminMode) {
					setVisible(true);
				} else {
					setVisible(false);
				}
				super.onBeforeRender();
			}
		});

		//Delete Link
		add(new Link("DeleteBlog") {
			private static final long serialVersionUID = 1L;

			protected final void onBeforeRender() {
				if (adminMode) {
					setVisible(true);
				} else {
					setVisible(false);
				}
				super.onBeforeRender();
			}

			public void onClick() {
				IBlogService blogService = ((IBlogServiceProvider) getPage()
						.getApplication()).getBlogService();
				blogService.deleteBlogEntry(blogEntry.getId());
				getParent().setVisible(false);
				getPage().render();
			}

		});

	}
}
