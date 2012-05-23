package org.emalan.cms.jcr.model;

import java.util.Map;

import org.apache.wicket.model.IChainingModel;
import org.apache.wicket.model.IModel;

public class NodeModelWrapper implements IChainingModel<ContentNode>, IPluginModel {
	private static final long serialVersionUID = 1L;

    protected JcrNodeModel nodeModel;

    public NodeModelWrapper(JcrNodeModel nodeModel) {
        this.nodeModel = nodeModel;
    }

    public JcrNodeModel getNodeModel() {
        return nodeModel;
    }

    public IModel<ContentNode> getChainedModel() {
        return nodeModel;
    }

	public void setChainedModel(IModel<?> model) {
        nodeModel = (JcrNodeModel) model;
	}

    public ContentNode getObject() {
        return nodeModel.getObject();
    }

    public void setObject(ContentNode object) {
        nodeModel.setObject(object);
    }

    public void detach() {
        if(nodeModel != null) {
            nodeModel.detach();
        }
    }

    // implement IPluginModel
    public Map<String, String> getMapRepresentation() {
        return nodeModel.getMapRepresentation();
    }


}
