package com.madalla.webapp.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.bo.page.IPageData;
import com.madalla.bo.page.ResourceData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.cms.AjaxEditableLink.ILinkData;

public class ContentLinkPanel extends Panel{
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public class LinkData implements ILinkData{

		private static final long serialVersionUID = 1L;
		
		private String name;
		private String title;
		private DynamicWebResource resource;
		
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
		
	}
	
	public ContentLinkPanel(final String id, final String nodeName) {
		super(id);
		
		log.debug("Editable Link Panel being created for node=" + nodeName + " id=" + id);
		IPageData page = getRepositoryservice().getPage(nodeName);
		final ResourceData resourceData = getRepositoryservice().getContentResource(page, id);
		final LinkData linkData = new LinkData(resourceData.getUrlDisplay(), resourceData.getUrlTitle(), resourceData.getResource());
		
		Panel editableLink = new AjaxEditableLink("contentLink", linkData){
			private static final long serialVersionUID = 1L;

			
        	@Override
			protected void onSubmit(AjaxRequestTarget target) {
				super.onSubmit(target);
				resourceData.setUrlDisplay(linkData.getName());
				getRepositoryservice().saveContentResource(resourceData);
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
