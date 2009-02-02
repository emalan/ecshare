package com.madalla.webapp.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.madalla.bo.IPageData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;

public class InlineContentPanel extends Panel {
    private static final long serialVersionUID = 1L;
    
    private Log log = LogFactory.getLog(this.getClass());

    /**
     * 
     * @param id
     * @param node
     * @param returnPage
     */
    public InlineContentPanel(final String id, final String nodeName) {
        super(id);
        
        log.debug("Content Panel being created for node=" + nodeName + " id=" + id);
        IPageData page = getRepositoryservice().getPage(nodeName);
        String contentBody = getRepositoryservice().getContentText(page, id , getSession().getLocale());

        
        final Model contentModel = new Model(contentBody);
        
        Panel editableLabel = new AjaxEditableLabel("contentText", contentModel){
			private static final long serialVersionUID = 1L;

        	@Override
			protected void onSubmit(AjaxRequestTarget target) {
				super.onSubmit(target);
				log.debug("onSubmit - value="+ contentModel.getObject());
			}
			
        	@Override
			protected void onBeforeRender(){
                IPageData page = getRepositoryservice().getPage(nodeName);
                String contentBody = getRepositoryservice().getContentText(page, id, getSession().getLocale());
                log.debug("onBeforeRender - setting new Content.");
                contentModel.setObject(contentBody);
                super.onBeforeRender();
            }            	
        };
        add(editableLabel);
        
    }

    private IRepositoryService getRepositoryservice(){
    	return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
    }

}
