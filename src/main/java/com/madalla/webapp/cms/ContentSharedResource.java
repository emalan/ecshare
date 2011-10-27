package com.madalla.webapp.cms;

import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.wicket.Application;
import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.SharedResources;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

import com.madalla.service.IDataService;
import com.madalla.wicket.resourcelink.EditableResourceLink.ILinkData;
import com.madalla.wicket.resourcelink.EditableResourceLink.ResourceType;

/**
 * Utility methods for managing the mounting of resources, so that they can have URL's that the browser
 * can interepret correctly. For example : pdf's
 *
 * @author Eugene Malan
 *
 */
public class ContentSharedResource {

    public static final String RESOURCE_PATH = "resource/";
    Logger log = LoggerFactory.getLogger(ContentSharedResource.class);

    public static void registerResource(WebApplication application, ILinkData data, String path, IDataService service){
        mountResource(application, data.getId(), data.getResourceType(), path, service);
    }

    public static String registerResource(WebApplication application, ILinkData data, IDataService service){
        if (data.getFileUpload() == null){
            return "";
        }
        String mountPath = RESOURCE_PATH + data.getFileUpload().getClientFileName();
        mountResource(application, data.getId(), data.getResourceType(), mountPath, service);
        return mountPath;
    }

    private static void mountResource(WebApplication application, String id, String resourceType, String path, IDataService service){
        SharedResources resources = application.getSharedResources();
        ResourceReference resourceReference = new ResourceReference(id);
        //remove previous
        resources.remove(id);
        application.unmount(path);

        resources.add(Application.class, id, null, null, createDynamicResource(id, resourceType, service));
        application.mountSharedResource(path, resourceReference.getSharedResourceKey());
    }

    private static Resource createDynamicResource(final String id, final String type, final IDataService service){
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
