package com.madalla.webapp.cms.admin;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.madalla.service.cms.jcr.Content;
import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.IRepositoryServiceProvider;

public class ContentDisplayPanel extends Panel {

	private static final long serialVersionUID = -3450362599578103637L;
	private Log log = LogFactory.getLog(this.getClass());
	private Component nodePath;
	private Component contentDisplay;
	private Content copiedContent;
	private String contentText = "" ;
	private String path = "";
	
	public ContentDisplayPanel(String name) {
		super(name);
		
		//Node path
		Model pathModel = new Model(){
			
			@Override
			public Object getObject(){
				return path;
			}
			
		};
		nodePath = new Label("nodeName", pathModel);
		nodePath.setOutputMarkupId(true);
		add(nodePath);
		
		//Content text
		Model textModel = new Model(){

			@Override
			public Object getObject() {
				return contentText;
			}
			
		};
		contentDisplay = new Label("contentText", textModel);
		contentDisplay.setOutputMarkupId(true);
		add(contentDisplay);
		
        //Delete Link
        Component delete = new Link("deleteNode"){
            private static final long serialVersionUID = 1L;
            
            protected final void onBeforeRender(){
                if (StringUtils.isEmpty(path) || !getContentService().isDeletableNode(path)){
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
        final Component copy = new Link("copyNode"){
            
			private static final long serialVersionUID = -1062211579369743790L;
			
			@Override
			protected final void onBeforeRender(){
				if (!StringUtils.isEmpty(path) && getContentService().isContentNode(path)){
					setEnabled(true);
				} else {
					setEnabled(false);
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
        final Component paste = new Link("pasteNode"){
			private static final long serialVersionUID = -4315390241296210531L;

			@Override
			protected final void onBeforeRender(){
				if (!StringUtils.isEmpty(path) && getContentService().isContentPasteNode(path) && copiedContent != null){
					setEnabled(true);
				} else {
					setEnabled(false);
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
		
		if (getContentService().isContentNode(path)){
			contentDisplay.modelChanging();
			contentText = getContentService().getContent(path).getText();
			contentDisplay.modelChanged();
		}
	}
	
	protected IRepositoryService getContentService() {
		return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
	}

}
