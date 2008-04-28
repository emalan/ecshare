package com.madalla.util.jcr;

public class AbstractTreeNode {
    static final Logger log = LoggerFactory.getLogger(AbstractTreeNode.class);

    private JcrTreeModel treeModel;
    private List<AbstractTreeNode> children = new ArrayList<AbstractTreeNode>();

    private boolean reloadChildren = true;
    private boolean reloadChildcount = true;
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

    public void markReload() {
        Iterator it = children.iterator();
        while (it.hasNext()) {
            AbstractTreeNode child = (AbstractTreeNode) it.next();
            child.markReload();
        }
        this.reloadChildren = true;
        this.reloadChildcount = true;
    }

    public Enumeration children() {
        ensureChildrenLoaded();
        return Collections.enumeration(children);
    }

    public TreeNode getChildAt(int i) {
        ensureChildrenLoaded();
        return children.get(i);
    }

    public int getChildCount() {
        ensureChildcountLoaded();
        return childcount;
    }

    public int getIndex(TreeNode node) {
        ensureChildrenLoaded();
        return children.indexOf(node);
    }

    public boolean isLeaf() {
        ensureChildcountLoaded();
        return childcount == 0;
    }

    public boolean getAllowsChildren() {
        return true;
    }

    protected abstract int loadChildcount() throws RepositoryException;

    protected abstract List<AbstractTreeNode> loadChildren() throws RepositoryException;

    public abstract String renderNode();

    private void ensureChildcountLoaded() {
        if (nodeModel == null || nodeModel.getNode() == null) {
            reloadChildren = false;
            reloadChildcount = false;
            childcount = 0;
        } else if (reloadChildcount) {
            try {
                childcount = loadChildcount();
            } catch (RepositoryException e) {
                log.error(e.getMessage());
            }
            reloadChildcount = false;
        }
    }


    private void ensureChildrenLoaded() {
        if (nodeModel.getNode() == null) {
            reloadChildren = false;
            reloadChildcount = false;
            children.clear();
        } else if (reloadChildren) {
            try {
                children = loadChildren();
                childcount = children.size();
            } catch (RepositoryException e) {
                log.error(e.getMessage());
            }
            reloadChildren = false;
            reloadChildcount = false;
        }
    }
}
