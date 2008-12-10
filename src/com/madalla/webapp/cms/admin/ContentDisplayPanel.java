package com.madalla.webapp.cms.admin;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.madalla.service.cms.AbstractBlogEntry;
import com.madalla.service.cms.IRepositoryService;
import com.madalla.service.cms.IRepositoryServiceProvider;
import com.madalla.service.cms.jcr.Content;

class ContentDisplayPanel extends Panel {

	private static final long serialVersionUID = -3450362599578103637L;
	private Log log = LogFactory.getLog(this.getClass());
	private Component nodePath;
	private Component contentDisplay;
	private Content copiedContent;
	private String contentText = "" ;
	private String path = "";
	Component paste;
	
	public ContentDisplayPanel(String name, final ContentAdminPanel parentPanel) {
		super(name);
		
		Model pathModel = new Model(){
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject(){
				return path;
			}
		};
		nodePath = new Label("nodeName", pathModel);
		nodePath.setOutputMarkupId(true);
		add(nodePath);
		
		Model textModel = new Model(){
			private static final long serialVersionUID = 1L;

			@Override
			public Object getObject() {
				return contentText;
			}
			
		};
		contentDisplay = new Label("contentText", textModel);
		contentDisplay.setOutputMarkupId(true);
		contentDisplay.setEscapeModelStrings(false);
		add(contentDisplay);
		
        //Delete Link
        Component delete = new IndicatingAjaxLink("deleteNode"){
            private static final long serialVersionUID = 1L;
            
            protected final void onBeforeRender(){
                if (StringUtils.isEmpty(path) || !getContentService().isDeletableNode(path)){
                    setEnabled(false);
                } else {
                    setEnabled(true);
                }
                super.onBeforeRender();
            }
            
			@Override
			public void onClick(AjaxRequestTarget target) {
            	getContentService().deleteNode(path);
            	refresh("");
            	target.addComponent(getParent());
            	parentPanel.refreshExplorerPanel();
            	target.addComponent(parentPanel.getExplorerPanel());
			}
        };
        delete.setOutputMarkupId(true);
        add(delete);

        //Paste Link
        paste = new IndicatingAjaxLink("pasteNode"){
			private static final long serialVersionUID = -4315390241296210531L;

			@Override
			protected final void onBeforeRender(){
				if (!StringUtils.isEmpty(path) && copiedContent != null && getContentService().isContentPasteNode(path)){
					setEnabled(true);
				} else {
					setEnabled(false);
				}
                super.onBeforeRender();
            }
            
			@Override
            public void onClick(AjaxRequestTarget target) {
			    //TODO
				//getContentService().pasteContent(path, copiedContent);
            	refresh("");
            	target.addComponent(getParent());
            	parentPanel.refreshExplorerPanel();
            	target.addComponent(parentPanel.getExplorerPanel());
            }
        };
        paste.setOutputMarkupId(true);
        add(paste);

        //Copy Link
        final Component copy = new IndicatingAjaxLink("copyNode"){
            
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
            public void onClick(AjaxRequestTarget target) {
				//TODO
			    //copiedContent = getContentService().getContent(path);
				target.addComponent(paste);
			}
        };
        copy.setOutputMarkupId(true);
        add(copy);

	}
	
	public Component getComponent(){
		return nodePath;
	}
	
	public void refresh(String path){
		log.debug("refresh - path="+path);
		nodePath.modelChanging();
		this.path = path;
		nodePath.modelChanged();

		contentDisplay.modelChanging();
		if (StringUtils.isEmpty(path)){
			contentText = "";
		} else if (getContentService().isContentNode(path)){
		    //TODO
			//contentText = getContentService().getContent(path).getText();
		} else if (getContentService().isBlogNode(path)){
			AbstractBlogEntry blogEntry = getContentService().getBlogEntry(path);
			StringBuffer sb = new StringBuffer("Title : ").append(blogEntry.getTitle()).append("<br/>")
				.append("Date : ").append(blogEntry.getDateTime()).append("<br/>").append("<br/>")
				.append(blogEntry.getText());
			contentText = sb.toString();
		}else if (getContentService().isImageNode(path)){
			contentText = "Image Node";
		} else {
			contentText = "";
		}
		contentDisplay.modelChanged();
		//paste.
		
	}
	
	protected IRepositoryService getContentService() {
		return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
	}

}
