package com.madalla.wicket.fileupload;

import java.io.InputStream;
import java.io.Serializable;

import org.apache.wicket.request.resource.ResourceReference;

/**
 * @author Eugene Malan
 *
 */
public interface ILinkData extends Serializable{

    String getId();

    /**
     * Name to be saved with resource. Client supplied.
     * 
     * @return
     */
    String getName();

    /**
     * Commonly used as title on link to Resource. Client supplied.
     * 
     * @return
     */
    String getTitle();

    ResourceReference getResourceReference();

    /**
     * Name of actual client file. Not persisted.
     * 
     * @return
     */
    String getFileName();
    
    InputStream getInputStream();

    FileUploadTypeType getResourceType();

    Boolean getHideLink();

    void setName(String name);

    void setTitle(String title);

    void setFileName(String fileName);
    
    void setFileInputStream(InputStream inputStream);

    void setResourceType(FileUploadTypeType type);

    void setHideLink(Boolean hide);

    String getUrl();

    void setUrl(String url);
    
}
