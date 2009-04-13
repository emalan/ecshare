package com.madalla.webapp.cms;

import org.apache.wicket.Application;
import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.SharedResources;
import org.apache.wicket.protocol.http.WebApplication;

public class ContentSharedResource {

    public static final String RESOURCE_PATH = "resource/";
    
    public static void registerResource(WebApplication application, String id, String fileName, Resource resource){
        SharedResources resources = application.getSharedResources();
        
        String mountPath = RESOURCE_PATH + fileName;
        ResourceReference resourceReference = new ResourceReference(id);

        //remove previous
        resources.remove(id);
        application.unmount(mountPath);

        resources.add(Application.class,id, null, null, resource);
        application.mountSharedResource(mountPath, resourceReference.getSharedResourceKey());
        
    }
    

}
