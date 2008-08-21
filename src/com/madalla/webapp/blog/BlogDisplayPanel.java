package com.madalla.webapp.blog;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.IBehavior;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.madalla.service.blog.BlogEntry;
import com.madalla.service.blog.IBlogService;
import com.madalla.service.blog.IBlogServiceProvider;

public class BlogDisplayPanel extends Panel{
	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(this.getClass());
	private int displayCount = 5;
	BlogEntry blogEntry;
	
	public BlogDisplayPanel(String id,String blog) {
		super(id);
		
        log.debug("construtor - retrieving blog entries from service.");
        IBlogService service = getBlogService();
        List<BlogEntry> blogList = service.getBlogEntries(blog);
        log.debug("construtor - retrieved blog entries. count="+blogList.size());
        ListView listView = new ListView("comments", blogList) {
			private static final long serialVersionUID = 1L;

			public void populateItem(final ListItem listItem) {
				final BlogEntry blogEntry = (BlogEntry) listItem.getModelObject();
                populateBlogEntryDisplay(listItem, blogEntry);
                listItem.add(new Label("title", new Model(blogEntry.getTitle())));
                listItem.add(new DateLabel("date", new Model(blogEntry.getDate()), new StyleDateConverter("MS",true)));
                listItem.add(new Label("keywords", new Model(blogEntry.getKeywords())));

                //more... link for single BlogDisplay
                IBehavior behavior = new AjaxEventBehavior("onclick") {
                	@Override
					protected void onEvent(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						
					}
                };
                
                CharSequence url = urlFor(behavior,Link.INTERFACE);
                log.debug("populateItem - url="+url);
                String htmlLink = "... <a href=\"" + url + "\">"+getString("label.more")+"</a>";
                
                listItem.add(new Label("textSummary", blogEntry.getSummary(htmlLink)).setEscapeModelStrings(false));
                listItem.add(new Label("textFull", blogEntry.getText()).setEscapeModelStrings(false));
            }
        };
        listView.setViewSize(displayCount);
        add(listView);

		blogEntry = blogList.get(0);
		add(new Label("title", new PropertyModel(blogEntry, "title")).setOutputMarkupId(true));
		add(new Label("title", new PropertyModel(blogEntry, "title")).setOutputMarkupId(true));
		add(new DateLabel("date", new PropertyModel(blogEntry, "date" ), new StyleDateConverter("MS",true)).setOutputMarkupId(true));
		add(new Label("keywords", new PropertyModel(blogEntry, "keywords")).setOutputMarkupId(true));
		add(new Label("text", new PropertyModel(blogEntry, "text")).setOutputMarkupId(true).setEscapeModelStrings(false));
	}
	
	public void changeModel(String blogEntryId){
		blogEntry = getBlogService().getBlogEntry(blogEntryId);
	}
	
	private void populateBlogEntryDisplay(final MarkupContainer blogDisplay, final BlogEntry blogEntry) {
	}
	
    private IBlogService getBlogService(){
    	return ((IBlogServiceProvider)getApplication()).getBlogService();
    }

	
}
