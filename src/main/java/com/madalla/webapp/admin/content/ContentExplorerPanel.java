package com.madalla.webapp.admin.content;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.tree.BaseTree;
import org.apache.wicket.markup.html.tree.ITreeState;
import org.apache.wicket.markup.html.tree.LinkIconPanel;
import org.apache.wicket.markup.html.tree.LinkTree;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.GenericBaseModel;
import org.emalan.cms.IRepositoryAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.jcr.model.tree.AbstractTreeNode;
import com.madalla.jcr.model.tree.JcrTreeModel;
import com.madalla.jcr.model.tree.JcrTreeNode;
import com.madalla.service.ApplicationService;
import com.madalla.service.IApplicationServiceProvider;

abstract class ContentExplorerPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private DefaultTreeModel treeModel;
	private boolean adminMode;
	private final BaseTree tree;
	private AbstractTreeNode currentNode;
	private AbstractTreeNode copiedNode;

	public ContentExplorerPanel(String name, final ContentAdminPanel parentPanel) {
		this(name, parentPanel, false);
	}

	public ContentExplorerPanel(String name, final ContentAdminPanel parentPanel, final boolean adminMode) {
		super(name);
		this.adminMode = adminMode;

		// Create Content Tree Model
		IModel<TreeModel> treeModelModel = new GenericBaseModel<TreeModel>(){
			private static final long serialVersionUID = 1L;

			@Override
			public TreeModel getObject() {
				if (treeModel == null){
					loadTreeData();
				}
				return treeModel;
			}

            @Override
            protected TreeModel createSerializableVersionOf(TreeModel object) {
            	if (treeModel == null){
            		loadTreeData();
            	}
                return treeModel;
            }
		};

		tree = new LinkTree("ContentTree", treeModelModel) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<?> getNodeTextModel(IModel<?> nodeModel) {
            	JcrTreeNode jcrTreeNode = (JcrTreeNode) nodeModel.getObject();
            	return new Model(jcrTreeNode.renderNode());
            }

			@Override
			protected Component newNodeComponent(String id, IModel<Object> model) {
				return new LinkIconPanel(id, model, this) {

					private static final long serialVersionUID = 1L;

					@Override
					protected void onNodeLinkClicked(Object node, BaseTree tree, AjaxRequestTarget target) {
						super.onNodeLinkClicked(node, tree, target);
						log.debug("onNodeLinkClicked - " + node);

						currentNode = (AbstractTreeNode) node;
						onNodeClicked(currentNode, target);
					}

					@Override
					protected Component newContentComponent(String componentId, BaseTree tree, IModel<?> model) {
						return new Label(componentId, getNodeTextModel(model));
					}

				};
			}

			@Override
			protected ITreeState newTreeState() {
				ITreeState treeState = super.newTreeState();
				Object root = getModelObject().getRoot();
				treeState.expandNode(root);
				return treeState;
			}


		};
		add(tree);

	}
	
   private ApplicationService getApplicationService() {
        return ((IApplicationServiceProvider) getApplication()).getApplicationService();
    }


    private IRepositoryAdminService getContentAdminService() {
        return getApplicationService().getRepositoryAdminService();
    }

	private void loadTreeData() {
		if (adminMode){
			treeModel = getApplicationService().getRepositoryContent();
		} else {
			treeModel =  getApplicationService().getSiteContent();
		}
	}

	boolean isCopyable(){
		return currentNode.isLeaf();
	}

	boolean isPasteable(){
		return copiedNode != null && !currentNode.isLeaf();
	}

	void copyNode(){
		copiedNode = currentNode;
	}

	void pasteNode(AjaxRequestTarget target){
		//move node in actual repository
		String srcPath = copiedNode.getObject().getPath();
		String destPath = currentNode.getObject().getPath() + "/" + copiedNode.getObject().getName();
		getContentAdminService().pasteNode(srcPath, destPath);
		copiedNode.getObject().setPath(destPath);

		//update tree
		JcrTreeModel treeModel = currentNode.getTreeModel();
		treeModel.removeNodeFromParent(copiedNode);
		treeModel.insertNodeInto(copiedNode, currentNode, currentNode.getChildCount());

		tree.updateTree(target);
		copiedNode = null;
	}

	public void deleteCurrentNode(AjaxRequestTarget target){
		getContentAdminService().deleteNode(currentNode.getObject().getPath());
		TreeNode parentNode = currentNode.getParent();
		JcrTreeModel treeModel = currentNode.getTreeModel();
		treeModel.removeNodeFromParent(currentNode);
		tree.getTreeState().selectNode(parentNode, true);
		tree.updateTree(target);
	}

	/**
	 * Parent will most likely want to handle Node Clicked Event
	 *
	 * @param currentNode
	 * @param target
	 */
	public abstract void onNodeClicked(AbstractTreeNode currentNode, AjaxRequestTarget target);

}
