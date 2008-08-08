package com.madalla.webapp.cms;

import javax.jcr.Node;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.madalla.service.cms.Content;
import com.madalla.service.cms.IContentData;
import com.madalla.service.cms.IContentService;
import com.madalla.service.cms.IContentServiceProvider;

public class ContentDisplayPanel extends Panel implements IContentData{

	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(this.getClass());
	private Component nodePath;
	private Component delete;
	private Component copy;
	private Component paste;
	private Content copiedContent;
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
		
        //Delete Link
        delete = new Link("deleteNode"){
            private static final long serialVersionUID = 1L;
            
            protected final void onBeforeRender(){
                if (StringUtils.isEmpty(path)){
                    setEnabled(false);
                } else {
                    setEnabled(true);
                }
                super.onBeforeRender();
            }
            
            public void onClick() {
            	getContentService().deleteNode(path);
            	path = "";
                setResponsePage(getPage().getClass());
            }
        };
        delete.setOutputMarkupId(true);
        add(delete);

        //Copy Link
        copy = new Link("copyNode"){
            
			private static final long serialVersionUID = -1062211579369743790L;
			
			@Override
			protected final void onBeforeRender(){
				if (!StringUtils.isEmpty(path) && getContentService().isContentNode(path)){
					copy.setEnabled(true);
				} else {
					copy.setEnabled(false);
				}
                super.onBeforeRender();
            }
            
			@Override
            public void onClick() {
				copiedContent = getContentService().getContent(path);
			}
        };
        copy.setOutputMarkupId(true);
        add(copy);
        
        //Paste Link
        paste = new Link("pasteNode"){
			private static final long serialVersionUID = -4315390241296210531L;

			@Override
			protected final void onBeforeRender(){
				if (!StringUtils.isEmpty(path) && getContentService().isContentPastNode(path) && copiedContent != null){
					paste.setEnabled(true);
				} else {
					paste.setEnabled(false);
				}
                super.onBeforeRender();
            }
            
			@Override
            public void onClick() {
				getContentService().pasteContent(path, copiedContent);
                path = "";
                setResponsePage(getPage().getClass());
            }
        };
        paste.setOutputMarkupId(true);
        add(paste);

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
