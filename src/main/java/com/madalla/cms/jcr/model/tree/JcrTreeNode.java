package com.madalla.cms.jcr.model.tree;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.madalla.cms.jcr.model.JcrNodeModel;

public class JcrTreeNode extends AbstractTreeNode{
	private static final long serialVersionUID = 1L;
	private final Log log = LogFactory.getLog(getClass());

    private final JcrTreeNode parent;

    public JcrTreeNode(JcrNodeModel nodeModel) {
        super(nodeModel);
        parent = null;
    }

    public JcrTreeNode(JcrNodeModel nodeModel, JcrTreeModel treeModel, JcrTreeNode parent) {
        super(nodeModel);
        setTreeModel(treeModel);
        treeModel.register(this);
        this.parent = parent;
    }

    //Do in session
    public void init(Node node) throws RepositoryException{
    	List<JcrTreeNode> children = loadChildren(node);
    	super.setChildren(children);
    }

    public TreeNode getParent() {
        return parent;
    }

    //init code
    private List<JcrTreeNode> loadChildren(Node node) throws RepositoryException {
        List<JcrTreeNode> newChildren = new ArrayList<JcrTreeNode>();
        for (NodeIterator jcrChildren = node.getNodes();jcrChildren.hasNext();) {
            Node jcrChild = jcrChildren.nextNode();
            if (jcrChild != null) {
                JcrNodeModel childModel = new JcrNodeModel(jcrChild);
                JcrTreeNode childTreeNode = new JcrTreeNode(childModel, getTreeModel(), this);
                childTreeNode.init(jcrChild);
                newChildren.add(childTreeNode);
            }
        }
        return newChildren;
    }

    @Override
	public String renderNode() {
        String result = nodeModel.render();
        return result;
    }

    @Override
	public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("nodeModel", nodeModel.toString())
                .toString();
    }

    @Override
	public boolean equals(Object object) {
        if (object instanceof JcrTreeNode == false) {
            return false;
        }
        if (this == object) {
            return true;
        }
        JcrTreeNode treeNode = (JcrTreeNode) object;
        return new EqualsBuilder().append(nodeModel, treeNode.nodeModel).isEquals();
    }

    @Override
	public int hashCode() {
        return new HashCodeBuilder(87, 335).append(nodeModel).toHashCode();
    }

	public void insert(MutableTreeNode node, int arg1) {
		List<JcrTreeNode> newChildren = new ArrayList<JcrTreeNode>();
		Enumeration<JcrTreeNode> children = children();
		while (children.hasMoreElements()){
			newChildren.add(children.nextElement());
		}
		newChildren.add((JcrTreeNode)node);
		setChildren(newChildren);
	}

	public void remove(int index) {
		log.debug("remove - index:"+ index);
		List<JcrTreeNode> newChildren = new ArrayList<JcrTreeNode>();
		Enumeration<JcrTreeNode> children = children();
		for (int i = 0 ; children.hasMoreElements(); i++){
			JcrTreeNode treeNode = children.nextElement();
			if (i != index){
				newChildren.add(treeNode);
			}
		}
		setChildren(newChildren);
	}

	public void remove(MutableTreeNode node) {
		log.debug("removeFormParent - "+ node);
	}

	public void removeFromParent() {
		log.debug("removeFormParent - ");
	}

	public void setParent(MutableTreeNode arg0) {
	}

	public void setUserObject(Object arg0) {
	}


}
