package com.madalla.util.jcr.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springmodules.jcr.JcrCallback;
import org.springmodules.jcr.JcrTemplate;


public class JcrNodeModel extends ItemModelWrapper implements IPluginModel {
    private static final long serialVersionUID = 1L;
    
    

    private transient boolean parentCached = false;
    private transient JcrNodeModel parent;

    public JcrNodeModel(JcrItemModel model, JcrTemplate jcrTemplate) {
        super(model);
        this.template = template;
    }

    public JcrNodeModel(Node node) {
        super(node);
    }
    
    public JcrNodeModel(String path) {
        super(path);
    }

    public JcrNodeModel(IPluginModel model) {
        super((String) model.getMapRepresentation().get("node"));
    }

    public Map<String, Object> getMapRepresentation() {
        Map map = new HashMap();
        map.put("node", itemModel.getPath());
        return map;
    }

    private Node getNode() {
        return (Node) itemModel.getObject();
    }
    
    public boolean hasNode(){
    	return itemModel.getObject() == null? false: true; 
    }
    
    public int getChildCount(){
    	NodeIterator iterator = getNodes();
    	return (int) iterator.getSize();
    }
    
    public NodeIterator getNodes(){
    	return (NodeIterator) template.execute(new JcrCallback(){

			public Object doInJcr(Session session) throws IOException,
					RepositoryException {
				
				Node node = getNode();
				if (!node.getSession().isLive()){
					node = (Node) session.getItem(node.getPath());
				}
				return node.getNodes();
			}
    		
    	});
    }

    public JcrNodeModel getParentModel() {
        if (!parentCached) {
            JcrItemModel parentModel = itemModel.getParentModel();
            if (parentModel != null) {
                parent = new JcrNodeModel(parentModel, template);
            } else {
                parent = null;
            }
            parentCached = true;
        }
        return parent;
    }

    public JcrNodeModel findRootModel() {
        JcrNodeModel result = this;
        while (result.getParentModel() != null) {
            result = result.getParentModel();
        }
        return result;
    }

    // override Object
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("itemModel", itemModel.toString())
                .toString();
    }

    public boolean equals(Object object) {
        if (object instanceof JcrNodeModel == false) {
            return false;
        }
        if (this == object) {
            return true;
        }
        JcrNodeModel nodeModel = (JcrNodeModel) object;
        return new EqualsBuilder().append(itemModel, nodeModel.itemModel).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder(57, 433).append(itemModel).toHashCode();
    }
}
