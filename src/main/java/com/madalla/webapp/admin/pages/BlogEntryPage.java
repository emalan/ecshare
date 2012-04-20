package com.madalla.webapp.admin.pages;

import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.madalla.webapp.admin.AbstractAdminPage;

@AuthorizeInstantiation("USER")
public class BlogEntryPage extends AbstractAdminPage {
    private static final long serialVersionUID = 1L;

    public BlogEntryPage(PageParameters parameters){
    	super(parameters);
    	String blogName = PageUtils.getPageParameter("0", parameters, "BlogEntryPage");
    	String blogEntryId = parameters.getString("1");
    	if (blogEntryId == null){
    		add(new BlogEntryPanel("adminPanel", blogName) );
    	} else {
    		add(new BlogEntryPanel("adminPanel", blogName, blogEntryId));
    	}


    }

}
