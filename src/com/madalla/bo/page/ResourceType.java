package com.madalla.bo.page;

public enum ResourceType {

    TYPE_PDF("application/pdf", "pdf", 10 * 1024), 
    TYPE_DOC("application/msword", "doc", 10 * 1024), 
    TYPE_ODT("application/vnd.oasis.opendocument.text", "odt", 10 * 1024);

    public final String resourceType;
    public final int bufferSize;
    public final String suffix;

    ResourceType(String type, String suffix, int bufferSize) {
        this.resourceType = type;
        this.suffix = suffix;
        this.bufferSize = bufferSize;
    }
}
