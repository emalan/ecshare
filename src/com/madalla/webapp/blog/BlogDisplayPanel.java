package com.madalla.webapp.blog;

import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.service.blog.IBlogService;

/**
 * The wicket application must implement the IBlogserviceProvider interface
 * @author exmalan
 *
 */
public class BlogDisplayPanel extends Panel implements IBlogAware  {

    private static final long serialVersionUID = 1L;
    
    
    public BlogDisplayPanel(String id, final Class blogEntryPage, final boolean adminMode, final IBlogService service) {
        super(id);
        
    }
    

}


