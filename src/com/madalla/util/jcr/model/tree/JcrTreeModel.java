package com.madalla.util.jcr.model.tree;

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultTreeModel;


public class JcrTreeModel extends DefaultTreeModel{

    private static final long serialVersionUID = 3495335448944545206L;
    private Map registry;
	
    public JcrTreeModel(AbstractTreeNode rootModel) {
        super(rootModel);
        rootModel.setTreeModel(this);

        registry = new HashMap();
        register(rootModel);
    }
    
    public void register(AbstractTreeNode treeNodeModel) {
        String key = treeNodeModel.getNodeModel().getContentNode().getPath();
        registry.put(key, treeNodeModel);
    }
    
}
