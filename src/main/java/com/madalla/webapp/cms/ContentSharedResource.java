package com.madalla.webapp.cms;

import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.Application;
import org.apache.wicket.SharedResources;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.ByteArrayResource;
import org.apache.wicket.request.resource.IResource;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.service.IDataService;
import com.madalla.wicket.resourcelink.EditableResourceLink.ILinkData;
import com.madalla.wicket.resourcelink.EditableResourceLink.LinkResourceType;

/**
 * Utility methods for managing the mounting of resources, so that they can have URL's that the browser
 * can interpret correctly. For example : pdf's
 *
 * @author Eugene Malan
 *
 */
public class ContentSharedResource {

    public static final String RESOURCE_PATH = "resource/";
    private static final Logger log = LoggerFactory.getLogger(ContentSharedResource.class);

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

    private static void mountResource(WebApplication application, String id, LinkResourceType type, String path, IDataService service){
    	
    	
    	
    	
        SharedResources sharedResources = application.getSharedResources();
        IResource resource = createDynamicResource(id, type, service);
        
        log.error("TODO - load resource");
        
        //TODO load one shared resource and use parameter to load image.
        
//        ResourceReference resourceReference = new ResourceReference(id);
//        
//        ResourceReference existing = sharedResources.get(Application.class, id, null, null, null, false);
//        sharedResources.remove(existing.)
//        
//        add(Application.class, name, null, null, null, resource);
//        
//        sharedResources.remove(key);
//        
//        sharedResources.add(name, resource);
//        
//        
//        ResourceReference resourceReference = new ResourceReference(id);
//        //remove previous
//        sharedResources.remove(id);
//        application.unmount(path);
//
//        sharedResources.add(Application.class, id, null, null, createDynamicResource(id, resourceType, service));
//        application.mountResource(path, resourceReference.getSharedResourceKey());
//        application.mountResource(path, reference);
    }

    private static IResource createDynamicResource(final String id, final LinkResourceType type, final IDataService service){

    	return new ByteArrayResource(type.mimeType) {
			private static final long serialVersionUID = 1L;
			
			@Override
			protected byte[] getData(Attributes attributes) {

				InputStream inputStream = service.getResourceStream(
                        id, "inputStream");
				byte[] ret;
				try {
					return IOUtils.toByteArray(inputStream);
				} catch (IOException e) {
					ret = "error".getBytes();
				    log.error("Unable to read resource. mimeType=" + type.mimeType + " id=" + id);
				}
				return ret;
			}
    		
    	};
    }


}
