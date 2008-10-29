package com.madalla.webapp.cms.admin;

import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.LinkIconPanel;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.madalla.service.cms.IRepositoryAdminService;
import com.madalla.service.cms.IRepositoryAdminServiceProvider;
import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.IRepositoryServiceProvider;
import com.madalla.util.jcr.model.ContentNode;
import com.madalla.util.jcr.model.IContentNode;
import com.madalla.util.jcr.model.tree.JcrTreeNode;

class ContentExplorerPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(this.getClass());
	private TreeModel treeModel;
	private boolean adminMode;

	public ContentExplorerPanel(String name, final ContentAdminPanel parentPanel) {
		this(name, parentPanel, false);
	}

	public ContentExplorerPanel(String name, final ContentAdminPanel parentPanel, final boolean adminMode) {
		super(name);
		this.adminMode = adminMode;
		refresh();
		
		// Create Content Tree Model
		IModel treeModelModel = new Model(){
			private static final long serialVersionUID = 1L;
			
			@Override
			public Object getObject() {
				return treeModel;
			}
		};
		
		BaseTree tree = new LinkTree("ContentTree", treeModelModel) {
			private static final long serialVersionUID = 1L;

			protected IModel getNodeTextModel(IModel model) {
				JcrTreeNode jcrTreeNode = (JcrTreeNode) model.getObject();
                return new Model(jcrTreeNode.renderNode());
			}
			
			protected Component newNodeComponent(String id, IModel model) {
				return new LinkIconPanel(id, model, this) {
					private static final long serialVersionUID = 1L;

					protected void onNodeLinkClicked(TreeNode node,
							BaseTree tree, AjaxRequestTarget target) {
						super.onNodeLinkClicked(node, tree, target);
						log.debug("onNodeLinkClicked - " + node);
						JcrTreeNode jcrTreeNode = (JcrTreeNode) node;
						if (jcrTreeNode.getObject() instanceof ContentNode) {
							IContentNode contentNode = (IContentNode) jcrTreeNode.getObject();
							String path = contentNode.getPath();
							log.debug("onNodeLinkClicked - path=" + path);
							parentPanel.refreshDisplayPanel(path);
							target.addComponent(parentPanel.getDisplayPanel());
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


	protected IRepositoryService getContentService() {
		return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
	}
    
    protected IRepositoryAdminService getContentAdminService() {
        return ((IRepositoryAdminServiceProvider) getApplication()).getRepositoryAdminService();
    }

	public void refresh() {
		if (adminMode){
			treeModel = getContentAdminService().getRepositoryContent();
		} else {
			treeModel = getContentAdminService().getSiteContent();
		}
		
	}

}
