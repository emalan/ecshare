package com.madalla.webapp.cms;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.util.resource.AbstractResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.ResourceStreamNotFoundException;

import com.madalla.bo.page.PageData;
import com.madalla.bo.page.ResourceData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.wicket.resourcelink.EditableResourceLink;
import com.madalla.wicket.resourcelink.EditableResourceLink.ILinkData;
import com.madalla.wicket.resourcelink.EditableResourceLink.ResourceType;

/**
 * Editable Link to a Respository Resource.
 * <p>
 * A link that will display a Repository Resource such as a PDF 
 * or a Word Doc. The Resource Link is editable when a valid user is logged.
 * The following is configurable:
 * <ul>
 * <li>Upload resource to Repository from desktop.</li>
 * <li>Change File Name that is displayed</li>
 * <li>Edit hover for link</li>
 * <li>Hide or show the link resource</li>
 * </ul>
 * 
 * </p>
 * @author Eugene Malan
 *
 */
public class ContentLinkPanel extends Panel{
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	/**
	 * This is used to pass data to {@link com.madalla.wicket.resourcelink.EditableResourceLink}
	 * 
	 * @author Eugene Malan
	 *
	 */
	public class LinkData implements ILinkData{

		private static final long serialVersionUID = 1L;
		
		private String name;
		private String title;
		private WebResource resource;
		private transient FileUpload fileUpload;
		private String resourceType;
		
		public String getName() {
			return name;
		}

		public WebResource getResource() {
			return resource;
		}
		
		public void setResource(WebResource resource){
			this.resource = resource;
		}

		public String getTitle() {
			return title;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public void setFileUpload(FileUpload fileUpload){
			this.fileUpload = fileUpload;
		}
		
		public FileUpload getFileUpload(){
			return fileUpload;
		}

		public String getResourceType() {
			return resourceType;
		}

		public void setResourceType(String resourceType) {
			this.resourceType = resourceType;
		}
		
	}
	
	public ContentLinkPanel(final String id, final String nodeName) {
		super(id);
		
		log.debug("Editable Link Panel being created for node=" + nodeName + " id=" + id);
		
		PageData page = getRepositoryservice().getPage(nodeName);
		final ResourceData resourceData = getRepositoryservice().getContentResource(page, id);
		log.debug("retrieved Resource data. " + resourceData);
		final LinkData linkData = new LinkData();
		linkData.setName(resourceData.getUrlDisplay());
		linkData.setTitle(resourceData.getUrlTitle());
		linkData.setResourceType(resourceData.getType());
		if (resourceData.getInputStream() != null) {
			linkData.setResource(new WebResource() {

				private static final long serialVersionUID = 1L;

				@Override
				public IResourceStream getResourceStream() {
					return new AbstractResourceStream() {

						private static final long serialVersionUID = 1L;
						
						@Override
						public String getContentType() {
							ResourceType resourceType = ResourceType.valueOf(resourceData.getType());
							if (resourceType != null){
								return resourceType.resourceType;
							}
							return null;
						}

						public void close() throws IOException {

						}

						public InputStream getInputStream()
								throws ResourceStreamNotFoundException {
							return getRepositoryservice().getResourceStream(
									resourceData.getId(), "inputStream");
						}

					};
				}

			});
		}
		
		Panel editableLink = new EditableResourceLink("contentLink", linkData){
			private static final long serialVersionUID = 1L;

        	@Override
			protected void onSubmit() {
				super.onSubmit();
				
				FileUpload upload = linkData.getFileUpload();
				if (upload == null){
					resourceData.setInputStream(null);
				} else {
					try {
						resourceData.setInputStream(upload.getInputStream());
						if (StringUtils.isEmpty(linkData.name)){
							linkData.name = upload.getClientFileName();
						}
					} catch (IOException e) {
						log.error("Error while handling File upload.", e);
					}
				}
				resourceData.setUrlDisplay(linkData.getName());
				resourceData.setUrlTitle(linkData.getTitle());
				resourceData.setType(linkData.getResourceType());
				log.debug("saving resource. " + resourceData);
				getRepositoryservice().saveContentResource(resourceData);
        	}
			
        	@Override
			protected void onBeforeRender(){
        		if (((IContentAdmin)getSession()).isLoggedIn()) {
        			this.setEditMode(true);
                } else {
                	this.setEditMode(false);
                }
                super.onBeforeRender();
            }            	
			
		};
		add(editableLink);
	}
	
    private IRepositoryService getRepositoryservice(){
    	return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
    }


}
