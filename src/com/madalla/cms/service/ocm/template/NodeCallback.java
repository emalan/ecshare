package com.madalla.cms.service.ocm.template;

import com.madalla.cms.bo.AbstractData;

public abstract class NodeCallback {
    public abstract void preSave(Object obj);
    public abstract void postSave();
    public abstract AbstractData createNew(String parentPath, String name);
}
