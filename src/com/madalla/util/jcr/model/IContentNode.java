package com.madalla.util.jcr.model;

public interface IContentNode {

    public abstract String getPath();
    
    public abstract Object getObject();

    public abstract String render();

}