package com.madalla.webapp.cms.admin;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.madalla.cms.jcr.model.ContentNode;
import com.madalla.cms.jcr.model.IContentNode;
import com.madalla.cms.jcr.model.tree.AbstractTreeNode;
import com.madalla.cms.jcr.model.tree.JcrTreeModel;
import com.madalla.cms.jcr.model.tree.JcrTreeNode;
import com.madalla.service.IDataService;
import com.madalla.service.IDataServiceProvider;
import com.madalla.service.IRepositoryAdminService;
import com.madalla.service.IRepositoryAdminServiceProvider;

class ContentExplorerPanel extends Panel {
	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(this.getClass());
	private DefaultTreeModel treeModel;
	private boolean adminMode;
	private final BaseTree tree;
	private AbstractTreeNode currentNode;

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
					getTreeData();
				}
				return treeModel;
			}

            @Override
            protected TreeModel createSerializableVersionOf(TreeModel object) {
            	if (treeModel == null){
            		getTreeData();
            	}
                return treeModel;
            }
		};
		
		tree = new LinkTree("ContentTree", treeModelModel) {
            private static final long serialVersionUID = 1L;

            @Override
            protected IModel<Object> getNodeTextModel(IModel<Object> nodeModel) {
                JcrTreeNode jcrTreeNode = (JcrTreeNode) nodeModel.getObject();
                return new Model(jcrTreeNode.renderNode());
            }
			
			protected Component newNodeComponent(String id, IModel<Object> model) {
				return new LinkIconPanel(id, model, this) {

				    private static final long serialVersionUID = 1L;

				    @Override
                    protected void onNodeLinkClicked(Object node, BaseTree tree,
                            AjaxRequestTarget target) {
                        super.onNodeLinkClicked(node, tree, target);
                        log.debug("onNodeLinkClicked - " + node);
                        
                        currentNode = (AbstractTreeNode) node;
                        if (currentNode.getObject() instanceof ContentNode) {
                            IContentNode contentNode = (IContentNode) currentNode.getObject();
                            String path = contentNode.getPath();
                            log.debug("onNodeLinkClicked - path=" + path);
                            
                            parentPanel.refreshDisplayPanel(path);
                            target.addComponent(parentPanel.getDisplayPanel());
                        }
				    }
					
					@Override
					protected Component newContentComponent(String componentId, BaseTree tree, IModel<Object> model) {
						return new Label(componentId, getNodeTextModel(model));
					}
				};
			}

			@Override
			protected ITreeState newTreeState() {
				ITreeState treeState = super.newTreeState();
				
				Object root = getModelObject().getRoot();
				Component rootComponent = getNodeComponent(root);
				treeState.expandNode(rootComponent);
				
				return treeState;
			}


		};
		
		add(tree);
		
		
		
		
	}


	protected IDataService getContentService() {
		return ((IDataServiceProvider) getApplication()).getRepositoryService();
	}
    
    protected IRepositoryAdminService getContentAdminService() {
        return ((IRepositoryAdminServiceProvider) getApplication()).getRepositoryAdminService();
    }

	private void getTreeData() {
		if (adminMode){
			treeModel = getContentAdminService().getRepositoryContent();
		} else {
			treeModel = getContentAdminService().getSiteContent();
		}
		
	}
	
	public void deleteCurrentNode(AjaxRequestTarget target){
		TreeNode parentNode = currentNode.getParent();
		JcrTreeModel treeModel = currentNode.getTreeModel();
		treeModel.removeNodeFromParent(currentNode);
		tree.getTreeState().selectNode(parentNode, true);
		refresh(target);
	}

	public void refresh(AjaxRequestTarget target) {
		tree.updateTree(target);
		
	}

}
