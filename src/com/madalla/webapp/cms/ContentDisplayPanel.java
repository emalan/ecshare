package com.madalla.webapp.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.service.cms.IContentData;
import com.madalla.service.cms.IContentService;
import com.madalla.service.cms.IContentServiceProvider;

public class ContentDisplayPanel extends Panel implements IContentData{

	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(this.getClass());
	
	public ContentDisplayPanel(String name) {
		super(name);
		
		final Label nodeName = new Label("nodeName","No Selection");
		nodeName.setOutputMarkupId(true);
		nodeName.add(new AjaxEventBehavior("onclick"){
			protected void onEvent(AjaxRequestTarget arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		add(nodeName);
	}
	
	public void refresh(String path){
		log.debug("refresh - path="+path);
	}
	
	protected IContentService getContentService() {
		return ((IContentServiceProvider) getApplication()).getContentService();
	}
    
}
