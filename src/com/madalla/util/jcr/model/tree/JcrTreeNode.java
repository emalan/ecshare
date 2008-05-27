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

    public JcrTreeNode(JcrNodeModel nodeModel) {
        super(nodeModel);
    }

    public JcrTreeNode(JcrNodeModel nodeModel, JcrTreeModel treeModel) {
        super(nodeModel);
        setTreeModel(treeModel);
        treeModel.register(this);
    }

    public TreeNode getParent() {
        JcrNodeModel parentModel = nodeModel.getParentModel();
        if (parentModel != null) {
            return getTreeModel().lookup(parentModel);
        }
        return null;
    }

    protected int loadChildcount() throws RepositoryException {
        int result = 1;
        Node node = nodeModel.getNode();
        result = (int) node.getNodes().getSize();
        return result;
    }

    protected List loadChildren() throws RepositoryException {
        Node node = nodeModel.getNode();
        List newChildren = new ArrayList();
        for (NodeIterator jcrChildren = node.getNodes();jcrChildren.hasNext();) {
            Node jcrChild = jcrChildren.nextNode();
            if (jcrChild != null) {
                JcrNodeModel childModel = new JcrNodeModel(jcrChild);
                JcrTreeNode treeNodeModel = new JcrTreeNode(childModel, getTreeModel());
                newChildren.add(treeNodeModel);
            }
        }
        return newChildren;
    }

    @Override
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
