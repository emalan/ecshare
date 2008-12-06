package com.madalla.util.jcr;

public abstract class NodeCallback {
    public abstract void preSave(Object obj);
    public abstract void postSave();
    public abstract Object createNew(String parentPath, String name);
}
