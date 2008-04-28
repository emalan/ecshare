package com.madalla.util.jcr.model.tree;

import java.util.HashMap;
import java.util.Map;

import javax.swing.tree.DefaultTreeModel;

import com.madalla.util.jcr.model.JcrNodeModel;


public class JcrTreeModel extends DefaultTreeModel{

	private Map registry;
	
    public JcrTreeModel(AbstractTreeNode rootModel) {
        super(rootModel);
        rootModel.setTreeModel(this);

        registry = new HashMap();
        register(rootModel);
 

    }
    
    public void register(AbstractTreeNode treeNodeModel) {
        String key = treeNodeModel.getNodeModel().getItemModel().getPath();
        registry.put(key, treeNodeModel);
    }
    
    public AbstractTreeNode lookup(JcrNodeModel nodeModel) {
        String key = nodeModel.getItemModel().getPath();
        if((AbstractTreeNode) registry.get(key) == null) {
            AbstractTreeNode parentNode = lookup(nodeModel.getParentModel());
            if(parentNode!=null) {
                // load children which get registered
                parentNode.children();
            } 
        }
        
        return (AbstractTreeNode) registry.get(key);
    }

}
