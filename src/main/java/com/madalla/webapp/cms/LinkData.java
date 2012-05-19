package com.madalla.webapp.cms;

import java.io.InputStream;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.wicket.request.resource.ResourceReference;

import com.madalla.wicket.fileupload.FileUploadTypeType;
import com.madalla.wicket.fileupload.ILinkData;

public class LinkData implements ILinkData {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String title;
    private String fileName;
    private ResourceReference resourceReference;
    private transient InputStream inputStream;
    private FileUploadTypeType resourceType;
    private Boolean hideLink;
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public void setResourceReference(ResourceReference resourceReference) {
        this.resourceReference = resourceReference;
    }

    public ResourceReference getResourceReference() {
        return resourceReference;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FileUploadTypeType getResourceType() {
        return resourceType;
    }

    public void setResourceType(FileUploadTypeType resourceType) {
        this.resourceType = resourceType;
    }

    public Boolean getHideLink() {
        return hideLink;
    }

    public void setHideLink(Boolean hideLink) {
        this.hideLink = hideLink;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this).toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
        
    }

}
