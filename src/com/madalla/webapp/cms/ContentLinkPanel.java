package com.madalla.webapp.cms;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.bo.page.PageData;
import com.madalla.bo.page.ResourceData;
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
	
	public ContentLinkPanel(final String id, final String nodeName) {
		super(id);
		
		log.debug("Editable Link Panel being created for node=" + nodeName + " id=" + id);
		PageData page = getRepositoryservice().getPage(nodeName);
		final ResourceData resourceData = getRepositoryservice().getContentResource(page, id);
		final LinkData linkData = new LinkData(resourceData.getUrlDisplay(), resourceData.getUrlTitle(), resourceData.getResource());
		
		Panel editableLink = new EditableResourceLink("contentLink", linkData){
			private static final long serialVersionUID = 1L;

			
        	@Override
			protected void onSubmit() {
				super.onSubmit();
				resourceData.setUrlDisplay(linkData.getName());
				resourceData.setUrlTitle(linkData.getTitle());
				
				try {
				//TODO
				FileUpload upload = linkData.getFileUpload();
				String contentType = upload.getContentType();
            	log.info("file upload - Content type="+contentType);
            	
            	//TODO create a validator
//            	if (!(contentType.equalsIgnoreCase("image/png") || contentType.equalsIgnoreCase("image/jpeg"))){
//            		log.warn("file upload - Input type not supported. Type="+contentType);
//            		warn(getString("error.type", new Model(upload)));
//            	}
            	InputStream inputStream = upload.getInputStream();
            	String imageName = StringUtils.deleteWhitespace(upload.getClientFileName());
            	imageName = StringUtils.substringBefore(imageName, ".");
				
				//TODO validate fileUpload and get input Stream
				resourceData.setInputStream(inputStream);
				getRepositoryservice().saveContentResource(resourceData);
				} catch (IOException e){
					//TODO catch and display this inside Editable Link
				}
			}
			
        	@Override
			protected void onBeforeRender(){

        		//TODO enable security
        		//if (((IContentAdmin)getSession()).isLoggedIn()) {
                //    setEnabled(true);
                //} else {
                //    setEnabled(false);
                //}
                super.onBeforeRender();
            }            	
			
		};
		add(editableLink);
	}
	
    private IRepositoryService getRepositoryservice(){
    	return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
    }


}
