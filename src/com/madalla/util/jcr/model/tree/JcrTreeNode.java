package com.madalla.util.jcr.model.tree;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.swing.tree.TreeNode;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.madalla.util.jcr.model.JcrNodeModel;

public class JcrTreeNode extends AbstractTreeNode{
	private static final long serialVersionUID = 1L;
    private JcrTreeNode parent = null;

    public JcrTreeNode(JcrNodeModel nodeModel) {
        super(nodeModel);
    }

    public JcrTreeNode(JcrNodeModel nodeModel, JcrTreeModel treeModel, JcrTreeNode parent) {
        super(nodeModel);
        setTreeModel(treeModel);
        treeModel.register(this);
        this.parent = parent;
    }
    
    //Do in session
    public void init(Node node) throws RepositoryException{
    	List children = loadChildren(node);
    	super.setChildren(children);
    }

    public TreeNode getParent() {
        return parent;
    }

    //init code
    private List loadChildren(Node node) throws RepositoryException {
        List newChildren = new ArrayList();
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

    public String renderNode() {
        //HippoNode node = getNodeModel().getNode();
        String result = "null";
//        if (node != null) {
//            try {
//                result = ISO9075Helper.decodeLocalName(node.getDisplayName());
//                if (node.hasProperty(HippoNodeType.HIPPO_COUNT)) {
//                    result += " [" + node.getProperty(HippoNodeType.HIPPO_COUNT).getLong() + "]";
//                }
//            } catch (RepositoryException e) {
//                result = e.getMessage();
//            }
//        }
        return result;
    }

    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("nodeModel", nodeModel.toString())
                .toString();
    }

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

    public int hashCode() {
        return new HashCodeBuilder(87, 335).append(nodeModel).toHashCode();
    }
}
