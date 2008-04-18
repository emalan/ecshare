package com.madalla.webapp.blog;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.MarkupContainer;
import org.apache.wicket.PageParameters;
import org.apache.wicket.datetime.StyleDateConverter;
import org.apache.wicket.datetime.markup.html.basic.DateLabel;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
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
    
	protected void populateBlogEntryDisplay(final MarkupContainer blogDisplay, final BlogEntry blogEntry,final Class blogEntryPage,
			final boolean adminMode) {
		blogDisplay.add(new DateLabel("date", new Model(blogEntry.getDate()), new StyleDateConverter("MS",true)));
        
        
        //Edit link
        Map params = new HashMap();
        params.put(BLOG_ENTRY_ID,new Integer(blogEntry.getId()));
        blogDisplay.add(new BookmarkablePageLink("EditBlog",blogEntryPage,new PageParameters(BLOG_ENTRY_ID+"="+blogEntry.getId())){
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
        
        //Delete Link
        blogDisplay.add(new Link("DeleteBlog"){
            private static final long serialVersionUID = 1L;
            protected final void onBeforeRender(){
                if (adminMode){
                    setVisible(true);
                } else {
                    setVisible(false);
                }
                super.onBeforeRender();
            }
            public void onClick() {
            	IBlogService blogService = getBlogService();
            	blogService.deleteBlogEntry(blogEntry.getId());
                getParent().setVisible(false);
                getPage().render();
            }
            
        });
	}
}


