package com.madalla.bo.page;

public enum ResourceType {

    TYPE_PDF("application/pdf", "pdf"), 
    TYPE_DOC("application/msword", "doc"), 
    TYPE_ODT("application/vnd.oasis.opendocument.text", "odt");

    public final String resourceType;
    public final String suffix;

    ResourceType(String type, String suffix) {
        this.resourceType = type;
        this.suffix = suffix;
    }
}
