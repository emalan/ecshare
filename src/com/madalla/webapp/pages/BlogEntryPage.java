package com.madalla.webapp.pages;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;

import com.madalla.webapp.AdminPage;
import com.madalla.webapp.blog.IBlogAware;
import com.madalla.webapp.blog.admin.BlogEntryPanel;

public class BlogEntryPage extends AdminPage implements IBlogAware {
    private static final long serialVersionUID = 1L;
    
    public BlogEntryPage(final PageParameters parameters){
        add(new BlogEntryPanel("blogEntryPanel", parameters));
    }
}
