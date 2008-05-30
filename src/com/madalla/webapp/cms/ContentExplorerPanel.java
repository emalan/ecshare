package com.madalla.webapp.cms;

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
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkIconPanel;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.madalla.service.blog.BlogEntry;
import com.madalla.service.cms.IContentData;
import com.madalla.service.cms.IContentService;
import com.madalla.service.cms.IContentServiceProvider;
import com.madalla.util.jcr.model.ContentNode;
import com.madalla.util.jcr.model.tree.JcrTreeNode;

public class ContentExplorerPanel extends Panel implements IContentData{
	private static final long serialVersionUID = 1L;

	private static DateFormat df = DateFormat.getDateInstance();

	private Log log = LogFactory.getLog(this.getClass());

	public ContentExplorerPanel(String name) {
		super(name);
		// List existing Blogs
		log.debug("construtor - retrieving blog entries from service.");
		IContentService service = getContentService();
		TreeModel treeModel = service.getSiteContent();
		log.debug("construtor - retrieved content entries. root="
				+ treeModel.getRoot());

		BaseTree tree = new LinkTree("ContentTree", treeModel) {
			private static final long serialVersionUID = 1L;

			protected IModel getNodeTextModel(IModel model) {
				JcrTreeNode jcrTreeNode = (JcrTreeNode) model.getObject();
				ContentNode contentNode = (ContentNode)jcrTreeNode.getObject();
				return new Model(contentNode.getName());
			}
			
			protected Component newNodeComponent(String id, IModel model) {
				return new LinkIconPanel(id, model, this) {
					private static final long serialVersionUID = 1L;

					protected void onNodeLinkClicked(TreeNode node,	BaseTree tree, AjaxRequestTarget target) {
						super.onNodeLinkClicked(node, tree, target);
						if (node.isLeaf()) {
							log.debug("onNodeLinkClicked - "+node);
							JcrTreeNode jcrTreeNode = (JcrTreeNode) node;
							if (jcrTreeNode.getObject() instanceof ContentNode){
								ContentNode contentNode = (ContentNode) jcrTreeNode.getObject();
								//TODO get parameters needed for editing content
								//TODO update a text entry area
							}
						}
					}

					protected Component newContentComponent(String componentId, BaseTree tree, IModel model) {
						return new Label(componentId, getNodeTextModel(model));
					}
				};
			}

		};
		tree.getTreeState().expandAll();
		add(tree);

	}

	protected IContentService getContentService() {
		return ((IContentServiceProvider) getApplication()).getContentService();
	}

}
