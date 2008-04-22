package com.madalla.webapp.blog;

import java.text.DateFormat;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkIconPanel;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.madalla.service.blog.BlogEntry;
import com.madalla.service.blog.IBlogService;
import com.madalla.util.ui.ITreeInput;

public class BlogArchivePanel extends AbstractBlogDisplayPanel {
	private static final long serialVersionUID = 1L;

	private static DateFormat df = DateFormat.getDateInstance();
	private Log log = LogFactory.getLog(this.getClass());

	public BlogArchivePanel(String id, final Class blogDisplayPage,
			Class blogEntryPage, final Class blogMainPage, boolean adminMode) {
		super(id);
		add(new BookmarkablePageLink("back", blogMainPage));
		//List existing Blogs
		log.debug("construtor - retrieving blog entries from service.");
		IBlogService service = getBlogService();
		TreeModel blogEntries = service.getBlogEntriesAsTree();
		log.debug("construtor - retrieved blog entries. root="
				+ blogEntries.getRoot());

		BaseTree tree = new LinkTree("BlogTree", blogEntries) {
			private static final long serialVersionUID = 1L;
			
			protected IModel getNodeTextModel(IModel model) {
				DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) model.getObject();
				if (treeNode.getUserObject() instanceof ITreeInput ){
					ITreeInput treeInput = (ITreeInput) treeNode.getUserObject();
					String title = treeInput.getTitle();
					return new Model(df.format(treeInput.getDate()) + (null == title?"":" - " + title));
				} else {
					return model;
				}
			}

			protected Component newNodeComponent(String id, IModel model) {
				return new LinkIconPanel(id, model, this) {
					private static final long serialVersionUID = 1L;

					protected void onNodeLinkClicked(TreeNode node,	BaseTree tree, AjaxRequestTarget target) {
						if (node.isLeaf()) {
							log.debug("onNodeLinkClicked - "+node);
							DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) node;
							if (treeNode.getUserObject() instanceof BlogEntry){
								BlogEntry blogEntry = (BlogEntry) treeNode.getUserObject();
								PageParameters pageParameters = new PageParameters(BLOG_ENTRY_ID + "=" + blogEntry.getId());
								setResponsePage(blogDisplayPage, pageParameters);
							}
						}
					}

					protected Component newContentComponent(String componentId, BaseTree tree, IModel model) {
						return new Label(componentId, getNodeTextModel(model));
					}
				};
			}

		};
		tree.getTreeState().collapseAll();
		add(tree);

	}

}
