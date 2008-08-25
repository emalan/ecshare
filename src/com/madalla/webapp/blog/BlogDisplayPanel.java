package com.madalla.webapp.blog;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.madalla.service.blog.IBlogService;
import com.madalla.service.blog.IBlogServiceProvider;
import com.madalla.service.cms.BlogEntry;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.pages.BlogEntryPage;

public class BlogDisplayPanel extends Panel implements IBlogAware{
	private static final long serialVersionUID = 1L;
	private final Log log = LogFactory.getLog(this.getClass());
	private static final int displayCount = 5;
	private final BlogEntryView blogEntry = new BlogEntryView();
	
	public BlogDisplayPanel(final String id, final String blog, final Class<? extends Page> returnPage) {
		super(id);
		
		final boolean adminMode = ((CmsSession)getSession()).isCmsAdminMode();
		
		//new Blog link
        add(new BookmarkablePageLink("createNew",BlogEntryPage.class, 
        		new PageParameters(RETURN_PAGE+"="+returnPage.getName()+","+BLOG_ENTRY_ID+"=,"+BLOG_NAME+"="+blog)){
    		private static final long serialVersionUID = -6335468391788102638L;
    		
    		@Override
			protected final void onBeforeRender(){
                if (adminMode){
                    setVisible(true);
                } else {
                    setVisible(false);
                }
                super.onBeforeRender();
            }
        });
		
        log.debug("construtor - retrieving blog entries from service.");
        List<BlogEntry> blogList = getBlogService().getBlogEntries(blog);
        log.debug("construtor - retrieved blog entries. count="+blogList.size());
        final ListView listView = new ListView("comments", blogList) {
			private static final long serialVersionUID = 1L;

			public void populateItem(final ListItem listItem) {
				final BlogEntry current = (BlogEntry) listItem.getModelObject();
                listItem.add(new Label("title", new Model(current.getTitle())));
                listItem.add(new DateLabel("date", new Model(current.getDate()), new StyleDateConverter("MS",true)));
                listItem.add(new Label("keywords", new Model(current.getKeywords())));

                //more... link for single BlogDisplay
//                IBehavior behavior = new AjaxEventBehavior("onclick") {
//                	@Override
//					protected void onEvent(AjaxRequestTarget target) {
//						// TODO Auto-generated method stub
//						
//					}
//                };
//                
//                CharSequence url = urlFor(behavior,Link.INTERFACE);
//                log.debug("populateItem - url="+url);
                
                final Component textSummary = new Label("textSummary", current.getSummary()).
                	setEscapeModelStrings(false).setOutputMarkupId(true);
                listItem.add(textSummary);

                final Component textFull = new Label("textFull", current.getText())
                	.setEscapeModelStrings(false).setOutputMarkupId(true);
                //textFull.setVisible(false);
                listItem.add(textFull);
                
                AjaxFallbackLink link = new AjaxFallbackLink("showFullText"){

					private static final long serialVersionUID = 8535809673244662238L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						target.addComponent(textSummary);
						target.addComponent(textFull);
						textSummary.setVisible(false);
						textFull.setVisible(true);
						this.setVisible(false);
					}
                	
                };
                listItem.add(link);

                //String script = "#";
                //String htmlLink = "... <a href=\"" + script + "\">"+getString("label.more")+"</a>";
                //textSummary.setModel(new Model(current.getSummary(htmlLink)));
                
                
                PageParameters params = new PageParameters(RETURN_PAGE+"="+returnPage.getName()+","+BLOG_ENTRY_ID+"="+current.getId()+","+BLOG_NAME+"="+blog);
                listItem.add(new BookmarkablePageLink("editBlog",BlogEntryPage.class, params){
                    
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
                
            }
        };
        listView.setViewSize(displayCount);
        add(listView);

        if (blogList.size() > 0){
        	blogEntry.init(blogList.get(0));
        }
		add(new Label("blogTitle", new PropertyModel(blogEntry, "title")).setOutputMarkupId(true));
		add(new DateLabel("date", new PropertyModel(blogEntry, "date" ), new StyleDateConverter("MS",true)).setOutputMarkupId(true));
		add(new Label("keywords", new PropertyModel(blogEntry, "keywords")).setOutputMarkupId(true));
		add(new Label("text", new PropertyModel(blogEntry, "text")).setOutputMarkupId(true).setEscapeModelStrings(false));
	}
	
	public void changeModel(String blogEntryId){
		BlogEntry newData = getBlogService().getBlogEntry(blogEntryId);
		blogEntry.init(newData);
		log.debug("changeModel - "+ blogEntry);
	}
	
    private IBlogService getBlogService(){
    	return ((IBlogServiceProvider)getApplication()).getBlogService();
    }

	
}
