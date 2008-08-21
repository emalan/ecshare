package com.madalla.webapp.blog;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.madalla.service.blog.BlogEntry;
import com.madalla.service.blog.IBlogService;
import com.madalla.service.blog.IBlogServiceProvider;

/**
 * The wicket application must implement the IBlogserviceProvider interface
 * @author exmalan
 *
 */
public abstract class AbstractBlogDisplayPanel extends Panel implements IBlogAware  {

    private static final long serialVersionUID = 1L;
    
    
    public AbstractBlogDisplayPanel(String id) {
        super(id);
        
    }
    
    /**
     * The wicket application must implement the IBlogserviceProvider interface for this to work
     * @return
     */
    protected IBlogService getBlogService(){
    	return ((IBlogServiceProvider)getApplication()).getBlogService();
    }
    
	protected void populateBlogEntryDisplay(final MarkupContainer blogDisplay, final BlogEntry blogEntry) {
		blogDisplay.add(new Label("title", new Model(blogEntry.getTitle())));
		blogDisplay.add(new DateLabel("date", new Model(blogEntry.getDate()), new StyleDateConverter("MS",true)));
		blogDisplay.add(new Label("keywords", new Model(blogEntry.getKeywords())));
	}
}


