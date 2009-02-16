package com.madalla.webapp.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.IPageData;
import com.madalla.bo.page.ResourceData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;

public class ContentLinkPanel extends Panel{
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public ContentLinkPanel(final String id, final String nodeName) {
		super(id);
		
		log.debug("Editable Link Panel being created for node=" + nodeName + " id=" + id);
		
		Panel editableLink = new AjaxEditableLink("contentLink"){
			private static final long serialVersionUID = 1L;

			private ResourceData resourceData;
			
        	@Override
			protected void onSubmit(AjaxRequestTarget target) {
				super.onSubmit(target);
				log.debug("onSubmit - value="+ resourceData);
			}
			
        	@Override
			protected void onBeforeRender(){
        		IPageData page = getRepositoryservice().getPage(nodeName);
        		resourceData = getRepositoryservice().getContentResource(page, id);
        		setModel(new PropertyModel(resourceData,"urlDisplay"));

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
