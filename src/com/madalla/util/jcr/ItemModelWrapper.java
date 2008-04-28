package com.madalla.util.jcr;

import javax.jcr.Item;

import org.apache.wicket.model.IChainingModel;
import org.apache.wicket.model.IModel;

public abstract class ItemModelWrapper implements IChainingModel {
    protected JcrItemModel itemModel;

    public ItemModelWrapper(JcrItemModel model) {
        itemModel = model;
    }

    public ItemModelWrapper(Item item) {
        itemModel = new JcrItemModel(item);
    }

    public ItemModelWrapper(String path) {
        itemModel = new JcrItemModel(path);
    }

    public JcrItemModel getItemModel() {
        return itemModel;
    }

    // Implement IChainingModel

    public IModel getChainedModel() {
        return itemModel;
    }

    public void setChainedModel(IModel model) {
        if (model instanceof JcrItemModel) {
            itemModel = (JcrItemModel) model;
        }
    }

    public Object getObject() {
        return itemModel.getObject();
    }

    public void setObject(Object object) {
        itemModel.setObject(object);
    }

    public void detach() {
        itemModel.detach();
    }
}
