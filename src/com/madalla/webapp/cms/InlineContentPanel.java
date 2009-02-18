package com.madalla.webapp.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.AjaxEditableLabel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.PageData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;

public class InlineContentPanel extends Panel {
    private static final long serialVersionUID = 1L;
    
    private Log log = LogFactory.getLog(this.getClass());
    
    /**
     * 
     * @param id
     * @param nodeName
     */
    public InlineContentPanel(final String id, final String nodeName) {
        super(id);
        
        log.debug("Content Panel being created for node=" + nodeName + " id=" + id);
        
        Panel editableLabel = new AjaxEditableLabel("contentText"){
			private static final long serialVersionUID = 1L;

			private ContentData contentData;
			
        	@Override
			protected void onSubmit(AjaxRequestTarget target) {
				super.onSubmit(target);
				log.debug("onSubmit - value="+ getModelObject());
	            log.debug("Submiting populated Content object to Content service.");
	            getRepositoryservice().saveContent(contentData);
	            info("Content saved to repository");
			}
			
        	@Override
			protected void onBeforeRender(){
                log.debug("onBeforeRender - setting new Content.");
                PageData page = getRepositoryservice().getPage(nodeName);
	            contentData = getRepositoryservice().getContent(page, id, getSession().getLocale());
                setModel(new PropertyModel(contentData, "text"));
                if (((IContentAdmin)getSession()).isLoggedIn()) {
                    setEnabled(true);
                } else {
                    setEnabled(false);
                }
                super.onBeforeRender();
            }            	
        };
        add(editableLabel);
        
    }

    private IRepositoryService getRepositoryservice(){
    	return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
    }

}
