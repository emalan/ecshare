package com.madalla.webapp.cms;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.protocol.http.WebApplication;

import com.madalla.bo.page.PageData;
import com.madalla.bo.page.ResourceData;
import com.madalla.service.IDataService;
import com.madalla.webapp.CmsSession;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.webapp.upload.FileUploadStatus;
import com.madalla.webapp.upload.IFileUploadStatus;
import com.madalla.wicket.resourcelink.EditableResourceLink;
import com.madalla.wicket.resourcelink.EditableResourceLink.ILinkData;

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
public class ContentLinkPanel extends CmsPanel{
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
		private ResourceReference resourceReference;
		private transient FileUpload fileUpload;
		private String resourceType;
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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
	}
	
	public ContentLinkPanel(final String id, final String nodeName) {
		super(id);

		add(Css.CSS_FORM);

		log.debug("Editable Link Panel being created for node=" + nodeName + " id=" + id);

			PageData page = getRepositoryService().getPage(nodeName);
			final ResourceData resourceData = getRepositoryService().getContentResource(page, id);
			log.debug("retrieved Resource data. " + resourceData);
			final LinkData linkData = createView(resourceData);
			if (!StringUtils.isEmpty(resourceData.getUrl())){
			    ContentSharedResource.registerResource((WebApplication)getApplication(), linkData, resourceData.getUrl(), getRepositoryService());
			}

			final EditableResourceLink editableLink = new EditableResourceLink("contentLink", linkData) {
				private static final long serialVersionUID = 1L;

				@Override
				protected void onSubmit() {
				    String mountPath = "";
				    if (linkData.getFileUpload() !=null){
	                    mountPath = ContentSharedResource.registerResource((WebApplication)getApplication(), linkData, getRepositoryService());
	                    linkData.setUrl(mountPath);
	                    //Start seperate thread so upload can continue if user navigates away
	                    final SubmitThread it = new SubmitThread(getAppSession(), linkData, getRepositoryService());
						it.start();
				    } else {
				    	formSubmit(getAppSession(), linkData, getRepositoryService());
				    }
				}

				@Override
				protected void onBeforeRender() {
					if (((IContentAdmin) getSession()).isLoggedIn()) {
						setEditMode(true);
					} else {
						setEditMode(false);
					}
					super.onBeforeRender();
				}

			};
			editableLink.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
			    private static final long serialVersionUID = -3131361470864509715L;

			    public String getObject() {
			        String cssClass;
			        if (((IContentAdmin)getSession()).isLoggedIn()) {
			            cssClass = "contentLinkEdit";
			        } else {
			            cssClass = "contentLink";
			        }
			        return cssClass;
			    }
			}));
			add(editableLink);
		}

	
    private LinkData createView(final ResourceData resourceData){
        LinkData linkData = new LinkData();
        linkData.setId(resourceData.getId());
        linkData.setName(resourceData.getUrlDisplay());
        linkData.setTitle(resourceData.getUrlTitle());
        linkData.setResourceType(resourceData.getType());
        linkData.setHideLink(resourceData.getHideLink());
        linkData.setUrl(resourceData.getUrl());
    	return linkData;
    }

    
    private CmsSession getAppSession(){
    	return (CmsSession) getSession();
    }

	/** This class does the file uploading and form submit */
	private static class SubmitThread extends Thread {
		private final CmsSession session;
		private final ILinkData data;
		private final IDataService service;

		public SubmitThread(CmsSession session, ILinkData data, IDataService service) {
			this.session = session;
			this.data = data;
			this.service = service;
		}

		public void run() {
			IFileUploadStatus uploadStatus = null;
			uploadStatus = new FileUploadStatus();
			session.setFileUploadStatus(data.getId(), uploadStatus);
			uploadStatus.setIsUploading(true);
			
			try {
				log.debug("Start processing...");
				ContentLinkPanel.formSubmit(session, data, service);

				// Sleep to simulate time-consuming work
//				try {
//					Thread.sleep(10000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
				log.debug("Done processing...");
				uploadStatus.setIsUploading(false);
			//} catch (InterruptedException e) {
			//	session.error(e.getMessage());
			} finally {
				//session.setFileUploadComplete(data.getId());

			}
		}
	}
	
    /**
     * static form submit that can be called from Thread
     * @param session
     * @param linkData
     */
    public static void formSubmit(CmsSession session, ILinkData linkData, IDataService service){
    	log.debug("onSubmit - submit Resource Form Data. " + linkData);
    	ResourceData resourceData = service.getContentResource(linkData.getId());
		FileUpload upload = linkData.getFileUpload();
		if (upload == null){
			resourceData.setInputStream(null);
			log.debug("onSubmit - setting InputStream to null");
		} else {
		    resourceData.setUrl(linkData.getUrl());
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
