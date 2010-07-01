package com.madalla.cms.jcr.model.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.madalla.cms.jcr.model.JcrNodeModel;
import com.madalla.cms.jcr.model.NodeModelWrapper;

public abstract class AbstractTreeNode extends NodeModelWrapper implements MutableTreeNode {
	private static final long serialVersionUID = 1L;

	static final Log log = LogFactory.getLog(AbstractTreeNode.class);

    private JcrTreeModel treeModel;
    private List<JcrTreeNode> children = new ArrayList<JcrTreeNode>();
    private int childcount = 0;

    public AbstractTreeNode(JcrNodeModel nodeModel) {
        super(nodeModel);
    }

    // The TreeModel that contains this AbstractTreeNode

    public void setTreeModel(JcrTreeModel treeModel) {
        this.treeModel = treeModel;
    }

    public JcrTreeModel getTreeModel() {
        return treeModel;
    }

    public Enumeration<JcrTreeNode> children() {
        return Collections.enumeration(children);
    }

    public TreeNode getChildAt(int i) {
        return (TreeNode) children.get(i);
    }

    public int getChildCount() {
        return childcount;
    }

    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    public boolean isLeaf() {
        return childcount == 0;
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public abstract String renderNode();

	public void setChildren(List<JcrTreeNode> children) {
		this.children = children;
		this.childcount = children.size();
	}
	

}
