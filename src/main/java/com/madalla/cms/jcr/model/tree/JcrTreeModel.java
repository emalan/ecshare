package com.madalla.cms.jcr.model.tree;

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;


public class JcrTreeModel extends DefaultTreeModel{

    private static final long serialVersionUID = 3495335448944545206L;
    private Map<String, AbstractTreeNode> registry;

    public JcrTreeModel(AbstractTreeNode rootModel) {
        super(rootModel);
        rootModel.setTreeModel(this);

        registry = new HashMap<String, AbstractTreeNode>();
        register(rootModel);
    }

    public void register(AbstractTreeNode treeNodeModel) {
        String key = treeNodeModel.getNodeModel().getContentNode().getPath();
        registry.put(key, treeNodeModel);
    }

	@Override
	public void nodesWereInserted(TreeNode parent, int[] childIndices) {
		  Object[] children = new Object[childIndices.length];
		  for (int i = 0; i < children.length; i++)
		      children[i] = getChild(parent, childIndices[i]);
		  fireTreeNodesInserted(this, getPathToRoot(parent), childIndices, children);
	}



}
