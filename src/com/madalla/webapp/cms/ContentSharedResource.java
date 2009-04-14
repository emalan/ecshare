package com.madalla.webapp.cms;

import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.Application;
import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.SharedResources;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

import com.madalla.service.IRepositoryService;
import com.madalla.wicket.resourcelink.EditableResourceLink.ILinkData;
import com.madalla.wicket.resourcelink.EditableResourceLink.ResourceType;

public class ContentSharedResource {

    public static final String RESOURCE_PATH = "resource/";
    
    public static void registerResource(WebApplication application, ILinkData data, IRepositoryService service){
        SharedResources resources = application.getSharedResources();
        
        
        ResourceReference resourceReference = new ResourceReference(data.getId());

        //remove previous
        resources.remove(data.getId());
        application.unmount(data.getPath());

        resources.add(Application.class,data.getId(), null, null, createDynamicResource(data.getId(), data.getResourceType(), service));
        application.mountSharedResource(data.getPath(), resourceReference.getSharedResourceKey());
        
    }
    
    private static Resource createDynamicResource(final String id, final String type, final IRepositoryService service){
        Resource resource = new WebResource() {
            private static final long serialVersionUID = 1L;

            @Override
            public IResourceStream getResourceStream() {
                return new AbstractResourceStream() {

                    private static final long serialVersionUID = 1L;
                    
                    @Override
                    public String getContentType() {
                        ResourceType resourceType = ResourceType.valueOf(type);
                        if (resourceType != null){
                            return resourceType.resourceType;
                        }
                        return null;
                    }

                    public void close() throws IOException {

                    }

                    public InputStream getInputStream() throws ResourceStreamNotFoundException {
                        InputStream inputStream = service.getResourceStream(
                                id, "inputStream");
                        return inputStream;
                    }

                };
            }

        };
        return resource;
    }
    

}
