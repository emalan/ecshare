package com.madalla.util.jcr.model.tree;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.RepositoryException;
import javax.swing.tree.TreeNode;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.madalla.util.jcr.model.JcrNodeModel;

public class LabelTreeNode extends AbstractTreeNode {
	private static final long serialVersionUID = 1L;

    private JcrNodeModel parentModel;
    private long moreCount;

    public LabelTreeNode(JcrNodeModel parentModel, JcrTreeModel treeModel, long moreCount) {
        super(parentModel);
        this.parentModel = parentModel;
        this.moreCount = moreCount;
        setTreeModel(treeModel);
    }

    public TreeNode getParent() {
        return getTreeModel().lookup(parentModel);
    }

    @Override
    protected int loadChildcount() throws RepositoryException {
        return 0;
    }

    @Override
    protected List<AbstractTreeNode> loadChildren() throws RepositoryException {
        return new ArrayList();
    }

    @Override
    public String renderNode() {
        return " ... " + moreCount + " more ...";
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("nodeModel", nodeModel.toString())
                .toString();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if(!(object instanceof LabelTreeNode)) {
            return false;
        } else {
            LabelTreeNode treeNode = (LabelTreeNode) object;
            return new EqualsBuilder().append(nodeModel, treeNode.nodeModel).isEquals();
        }
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(87, 335).append(nodeModel).toHashCode();
    }
}
