package com.madalla.service.cms.ocm.template;

import com.madalla.service.cms.AbstractData;

public abstract class NodeCallback {
    public abstract void preSave(Object obj);
    public abstract void postSave();
    public abstract AbstractData createNew(String parentPath, String name);
}
