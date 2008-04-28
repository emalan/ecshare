package com.madalla.util.jcr;

import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class JcrNodeModel extends ItemModelWrapper implements IPluginModel {
    private static final long serialVersionUID = 1L;

    private transient boolean parentCached = false;
    private transient JcrNodeModel parent;

    public JcrNodeModel(JcrItemModel model) {
        super(model);
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

    public HippoNode getNode() {
        return (HippoNode) itemModel.getObject();
    }

    public JcrNodeModel getParentModel() {
        if (!parentCached) {
            JcrItemModel parentModel = itemModel.getParentModel();
            if (parentModel != null) {
                parent = new JcrNodeModel(parentModel);
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("itemModel", itemModel.toString())
                .toString();
    }

    @Override
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder(57, 433).append(itemModel).toHashCode();
    }
}
