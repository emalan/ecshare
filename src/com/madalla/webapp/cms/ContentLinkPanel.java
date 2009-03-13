package com.madalla.webapp.cms;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
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
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.css.Css;
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
	private static final Log log = LogFactory.getLog(ContentLinkPanel.class);
	
	/**
	 * This is used to pass data to {@link com.madalla.wicket.resourcelink.EditableResourceLink}
	 * 
	 * @author Eugene Malan
	 *
	 */
	public class LinkData implements ILinkData{

		private static final long serialVersionUID = 1L;
		
		private String id;
		private String name;
		private String title;
		private WebResource resource;
		private transient FileUpload fileUpload;
		private String resourceType;
		private Boolean hideLink;
		
		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

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

		public Boolean getHideLink() {
			return hideLink;
		}

		public void setHideLink(Boolean hideLink) {
			this.hideLink = hideLink;
		}
		
		public String toString() {
	        return ReflectionToStringBuilder.toString(this).toString();
	    }
	}
	
	public ContentLinkPanel(final String id, final String nodeName) {
		super(id);

		add(Css.CSS_FORM);

		log.debug("Editable Link Panel being created for node=" + nodeName + " id=" + id);

			PageData page = getRepositoryservice().getPage(nodeName);
			final ResourceData resourceData = getRepositoryservice().getContentResource(page, id);
			log.debug("retrieved Resource data. " + resourceData);
			final LinkData linkData = createView(resourceData);

			Panel editableLink = new EditableResourceLink("contentLink", linkData) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit() {
					// Start a thread that will continue running even if the
					// user goes to another page.
					final SubmitThread it = new SubmitThread(getAppSession(), linkData, getRepositoryservice());
					it.start();
					// Refresh the page in order to disable the form field and
					// buttons.
				}

				@Override
				protected void onBeforeRender() {
					if (((IContentAdmin) getSession()).isLoggedIn()) {
						this.setEditMode(true);
					} else {
						this.setEditMode(false);
						if (linkData.getHideLink() != null && linkData.getHideLink().equals(Boolean.TRUE)) {
							log.debug("onBeforeRender - hiding contentLink.");
							setVisible(false);
						}
					}
					super.onBeforeRender();
				}

			};
			add(editableLink);
		}

	
    private LinkData createView(final ResourceData resourceData){
        LinkData linkData = new LinkData();
        linkData.setId(resourceData.getId());
        linkData.setName(resourceData.getUrlDisplay());
        linkData.setTitle(resourceData.getUrlTitle());
        linkData.setResourceType(resourceData.getType());
        linkData.setHideLink(resourceData.getHideLink());
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

                        public InputStream getInputStream() throws ResourceStreamNotFoundException {
                        	log.debug("getInputStream - " + resourceData);
                            InputStream inputStream = getRepositoryservice().getResourceStream(
                                    resourceData.getId(), "inputStream");
                            log.debug("getInputStream - done.");
                            return inputStream;
                        }

                    };
                }

            });
        }

    	return linkData;
    }

    private IRepositoryService getRepositoryservice(){
    	return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
    }
    
    private CmsSession getAppSession(){
    	return (CmsSession) getSession();
    }

	/** This class does the file uploading and form submit */
	private static class SubmitThread extends Thread {
		private final CmsSession session;
		private final ILinkData data;
		private final IRepositoryService service;

		public SubmitThread(CmsSession session, ILinkData data, IRepositoryService service) {
			this.session = session;
			this.data = data;
			this.service = service;
		}

		public void run() {
			session.setIsUploading(true);
			try {
				log.debug("Start processing...");
				ContentLinkPanel.formSubmit(session, data, service);

				// Sleep to simulate time-consuming work
				//Thread.sleep(10000);
				log.debug("Done processing...");
				session.setUploadComplete(true);
//			} catch (InterruptedException e) {
//				session.error(e.getMessage());
			} finally {
				session.setIsUploading(false);
			}
		}
	}
	
    /**
     * static form submit that can be called from Thread
     * @param session
     * @param linkData
     */
    public static void formSubmit(CmsSession session, ILinkData linkData, IRepositoryService service){
    	log.debug("onSubmit - submit Resource Form Data. " + linkData);
    	ResourceData resourceData = service.getContentResource(linkData.getId());
		FileUpload upload = linkData.getFileUpload();
		if (upload == null){
			resourceData.setInputStream(null);
			log.debug("onSubmit - setting InputStream to null");
		} else {
			try {
				log.debug("onSubmit - uploading File...");
				resourceData.setInputStream(upload.getInputStream());
				if (StringUtils.isEmpty(linkData.getName())){
					linkData.setName(upload.getClientFileName());
				}
				log.debug("onSubmit - uploading File... done");
			} catch (IOException e) {
				log.error("Error while handling File upload.", e);
			}
		}
		resourceData.setUrlDisplay(linkData.getName());
		resourceData.setUrlTitle(linkData.getTitle());
		resourceData.setType(linkData.getResourceType());
		resourceData.setHideLink(linkData.getHideLink());
		log.debug("onSubmit - saving resource. " + resourceData);
		service.saveContentResource(resourceData);
		log.debug("onSubmit - done..");
    }

}
