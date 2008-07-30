package com.madalla.webapp.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.madalla.service.cms.IContentData;
import com.madalla.service.cms.IContentService;
import com.madalla.service.cms.IContentServiceProvider;

public class ContentDisplayPanel extends Panel implements IContentData{

	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(this.getClass());
	private Component nodePath;
	private String path = "";
	
	public ContentDisplayPanel(String name) {
		super(name);
		Model model = new Model(){
			public Object getObject(){
				return path;
			}
		};
		nodePath = new Label("nodeName", model);
		nodePath.setOutputMarkupId(true);
		add(nodePath);
	}
	
	public Component getComponent(){
		return nodePath;
	}
	
	public void refresh(String path){
		log.debug("refresh - path="+path);
		nodePath.modelChanging();
		this.path = path;
		nodePath.modelChanged();
	}
	
	protected IContentService getContentService() {
		return ((IContentServiceProvider) getApplication()).getContentService();
	}
    
}
