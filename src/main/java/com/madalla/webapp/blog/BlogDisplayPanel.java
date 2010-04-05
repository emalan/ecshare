package com.madalla.webapp.blog;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.behavior.StringHeaderContributor;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.madalla.bo.blog.BlogData;
import com.madalla.bo.blog.BlogEntryData;
import com.madalla.bo.blog.IBlogEntryData;
import com.madalla.service.IDataService;
import com.madalla.service.IDataServiceProvider;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.admin.AdminPage;
import com.madalla.webapp.login.aware.LoginAwareAdminLink;
import com.madalla.webapp.pages.BlogEntryPage;

public class BlogDisplayPanel extends Panel {
	private static final long serialVersionUID = 1L;
	private static final String META_NAME = "<meta name=\"{0}\" content=\"{1}\"/>";

	private final Log log = LogFactory.getLog(this.getClass());
	private int displayCount = 5;
	private final BlogEntryView blogEntry = new BlogEntryView();
	
	BlogDisplayPanel(final String id, final BlogData blog, final String blogEntryId){
		super(id);
		init(id, blog );
		changeModel(blogEntryId);
		add(new SimpleAttributeModifier("class","showBlog"));
		add(new StringHeaderContributor(MessageFormat.format(META_NAME, "keywords", blogEntry.getKeywords())));
	}
	BlogDisplayPanel(final String id, final BlogData blog) {
		super(id);
		init(id, blog);
		//TODO Store the Blog Home Meatadata keywords in CMS
		//TODO Change the Content Entry to a Blog intro and keywords
		blogEntry.setKeywords("Eugene Malan, Eugene, Malan, Blog, CMS, Wicket, Java,"+blogEntry.getBlog());
		add(new StringHeaderContributor(MessageFormat.format(META_NAME, "keywords", blogEntry.getKeywords())));
	}
	
	private void init(final String id, final BlogData blog) {
		final boolean adminMode = ((CmsSession)getSession()).isCmsAdminMode();
		
		//new Blog link
		add(new LoginAwareAdminLink("createNew", BlogEntryPage.class, true, true){
			private static final long serialVersionUID = 1L;

			@Override
			protected AdminPage constructAdminPage(Class<? extends AdminPage> clazz) {
				return new BlogEntryPage(blog.getName());
			}
			
		});
		
        log.debug("construtor - retrieving blog entries from service.");
        List<BlogEntryData> blogList = getRepositoryService().getBlogEntries(blog);

        log.debug("construtor - retrieved blog entries. count="+blogList.size());
        
        //ListView repeater
        final ListView<BlogEntryData> listView = new ListView<BlogEntryData>("comments", blogList) {
			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(final ListItem<BlogEntryData> listItem) {
				final BlogEntryData current = listItem.getModelObject();
                listItem.add(new Label("title", new Model<String>(current.getTitle())));
                listItem.add(new DateLabel("date", new Model<Date>(current.getDate().toDate()), new StyleDateConverter("MS",true)));
                listItem.add(new Label("keywords", new Model<String>(current.getKeywords())));

                final Component textSummary = new Label("textSummary", current.getSummary()).
                	setEscapeModelStrings(false).setOutputMarkupId(true);
                listItem.add(textSummary);

                final Component textFull = new Label("textFull", current.getText())
                	.setEscapeModelStrings(false).setOutputMarkupId(true);	
                listItem.add(textFull);
                
                AjaxFallbackLink<Object> link = new AjaxFallbackLink<Object>("showFullText"){
					private static final long serialVersionUID = 8535809673244662238L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						target.addComponent(textFull);
						target.addComponent(textSummary);
						textSummary.add(new SimpleAttributeModifier("class","blogTextHide"));
						textFull.add(new SimpleAttributeModifier("class","blogText"));
						add(new SimpleAttributeModifier("class","blogTextHide"));
						target.addComponent(this);
					}
                	
                };
                link.setOutputMarkupId(true);
                listItem.add(link);

                //String script = "#";
                //String htmlLink = "... <a href=\"" + script + "\">"+getString("label.more")+"</a>";
                //textSummary.setModel(new Model(current.getSummary(htmlLink)));
                
                listItem.add(new LoginAwareAdminLink("editBlog", BlogEntryPage.class, true, true){
					private static final long serialVersionUID = 1L;

					@Override
					protected AdminPage constructAdminPage(Class<? extends AdminPage> clazz) {
						return new BlogEntryPage(blog.getName(), current.getId());
					}
                	
                });
                
            }
        };
        listView.setViewSize(displayCount);
        add(listView);

        if (blogList.size() > 0){
        	blogEntry.init(blogList.get(0));
        }
		add(new Label("blogTitle", new PropertyModel<String>(blogEntry, "title")).setOutputMarkupId(true));
		add(new DateLabel("date", new PropertyModel<Date>(blogEntry, "date" ), new StyleDateConverter("MS",true)).setOutputMarkupId(true));
		add(new Label("keywords", new PropertyModel<String>(blogEntry, "keywords")).setOutputMarkupId(true));
		add(new Label("text", new PropertyModel<String>(blogEntry, "text")).setOutputMarkupId(true).setEscapeModelStrings(false));
		
		add(new LoginAwareAdminLink("editBlogLink", BlogEntryPage.class, true, true){
			private static final long serialVersionUID = 1L;

			@Override
			protected AdminPage constructAdminPage(Class<? extends AdminPage> clazz) {
				return new BlogEntryPage(blog.getName(), blogEntry.getId());
			}
			
		});

	}
	
	public void changeModel(String blogEntryId){
		IBlogEntryData newData = getRepositoryService().getBlogEntry(blogEntryId);
		blogEntry.init(newData);
		log.debug("changeModel - "+ blogEntry);
	}
	
    private IDataService getRepositoryService(){
    	return ((IDataServiceProvider)getApplication()).getRepositoryService();
    }

	
}
