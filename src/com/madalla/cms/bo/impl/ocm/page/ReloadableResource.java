package com.madalla.cms.bo.impl.ocm.page;

import java.io.IOException;
import java.io.InputStream;

import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

import com.madalla.cms.service.ocm.template.RepositoryTemplate;

public class ReloadableResource extends WebResource{

    private static final long serialVersionUID = 1L;
    
    final private RepositoryTemplate template;
    final private String path;
    final private String propertyName;
    
    public ReloadableResource(String path, String propertyName, RepositoryTemplate template){
        this.path = path;
        this.propertyName = propertyName;
        this.template = template;
    }
    
    @Override
    public IResourceStream getResourceStream() {

        final InputStream inputStream =  template.getNodePropertyStream(path, propertyName);
        
        return new AbstractResourceStream() {

            private static final long serialVersionUID = 1L;

            public void close() throws IOException {
                inputStream.close();
            }

            public InputStream getInputStream() throws ResourceStreamNotFoundException {
                return inputStream;
            }

            
        };
    }

}
