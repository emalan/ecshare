package com.madalla.webapp.cms;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.bo.page.PageData;
import com.madalla.bo.page.ResourceData;
import com.madalla.bo.page.ResourceType;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.cms.EditableResourceLink.ILinkData;

public class ContentLinkPanel extends Panel{
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public class LinkData implements ILinkData{

		private static final long serialVersionUID = 1L;
		
		private String name;
		private String title;
		private DynamicWebResource resource;
		private transient FileUpload fileUpload;
		
		/**  Data object that needs to be passed to AjaxEditableLink **/
		public LinkData(String name, String title, DynamicWebResource resource){
			this.name = name;
			this.title = title;
			this.resource = resource;
		}

		public String getName() {
			return name;
		}

		public DynamicWebResource getResource() {
			return resource;
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
		
	}
	
	public ContentLinkPanel(final String id, final String nodeName, ResourceType type) {
		super(id);
		
		log.debug("Editable Link Panel being created for node=" + nodeName + " id=" + id);
		
		PageData page = getRepositoryservice().getPage(nodeName);
		final ResourceData resourceData = getRepositoryservice().getContentResource(page, id, type);
		final LinkData linkData = new LinkData(resourceData.getUrlDisplay(), resourceData.getUrlTitle(), resourceData.getResource());
		
		Panel editableLink = new EditableResourceLink("contentLink", linkData){
			private static final long serialVersionUID = 1L;

			
        	@Override
			protected void onSubmit() {
				super.onSubmit();
				resourceData.setUrlDisplay(linkData.getName());
				resourceData.setUrlTitle(linkData.getTitle());
				
				try {
					// TODO create a validator
					// if (!(contentType.equalsIgnoreCase("image/png") ||
					// contentType.equalsIgnoreCase("image/jpeg"))){
					// log.warn("file upload - Input type not supported. Type="+contentType);
					// warn(getString("error.type", new Model(upload)));
					// }

					// Transfer values and save
					FileUpload upload = linkData.getFileUpload();
					resourceData.setInputStream(upload.getInputStream());
					resourceData.setUrlDisplay(StringUtils
							.isEmpty(linkData.name) ? upload
							.getClientFileName() : linkData.name);
					resourceData.setUrlTitle(linkData.title);
					
					getRepositoryservice().saveContentResource(resourceData);
					upload.closeStreams();
				} catch (IOException e) {
					// TODO catch and display this inside Editable Link
				}
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
