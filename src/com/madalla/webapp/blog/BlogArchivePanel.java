package com.madalla.webapp.blog;

import javax.swing.tree.TreeModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkTree;

import com.madalla.service.blog.IBlogService;

public class BlogArchivePanel extends AbstractBlogDisplayPanel {
	private static final long serialVersionUID = 1L;
	private Log log = LogFactory.getLog(this.getClass());

	public BlogArchivePanel(String id, Class blogDisplayPage , Class blogEntryPage, boolean adminMode) {
		super(id);
		
        //List existing Blogs
        log.debug("construtor - retrieving blog entries from service.");
        IBlogService service = getBlogService();
        TreeModel blogEntries = service.getBlogEntriesAsTree();
        log.debug("construtor - retrieved blog entries. root="+blogEntries.getRoot());
        
        BaseTree tree = new LinkTree("BlogTree",blogEntries);
        tree.getTreeState().collapseAll();
        add(tree);
        

	}

}
