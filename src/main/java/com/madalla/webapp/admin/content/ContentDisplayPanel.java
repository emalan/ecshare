package com.madalla.webapp.admin.content;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.emalan.cms.IDataService;
import org.emalan.cms.IDataServiceProvider;
import org.emalan.cms.IRepositoryAdminService;
import org.emalan.cms.IRepositoryAdminServiceProvider;
import org.emalan.cms.jcr.NodeDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataRetrievalFailureException;

class ContentDisplayPanel extends Panel {

	private static final long serialVersionUID = -3450362599578103637L;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private Component nodePath;
	private Component contentDisplay;
	private String path = "";
	private Component paste;

	public ContentDisplayPanel(String name, final ContentAdminPanel parentPanel) {
		super(name);

		Model<String> pathModel = new Model<String>(){
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject(){
				return path;
			}
		};


		nodePath = new Label("nodeName", pathModel);
		nodePath.setOutputMarkupId(true);
		add(nodePath);

		Model<String> textModel = new Model<String>(){
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				if (StringUtils.isEmpty(path)){
					return "";
				} else {
					try {
						NodeDisplay display = getAdminService().getNodeDisplay(path);
						return displayNode(display);
					} catch (DataRetrievalFailureException e){
						log.error("Failure retrieving Node. path=" + path);
						return "Error!";
					}
				}
			}

		};
		contentDisplay = new Label("contentText", textModel);
		contentDisplay.setOutputMarkupId(true);
		contentDisplay.setEscapeModelStrings(false);
		add(contentDisplay);

        //Delete Link
        Component delete = new IndicatingAjaxLink<Object>("deleteNode"){
            private static final long serialVersionUID = 1L;

            @Override
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
            	//getContentService().deleteNode(path);
            	refresh("");
            	//target.addComponent(getParent());
            	parentPanel.deleteNode(target);
            	//target.addComponent(parentPanel.getExplorerPanel());
			}
        };
        delete.setOutputMarkupId(true);
        add(delete);

        //Paste Link
        paste = new IndicatingAjaxLink<Object>("pasteNode"){
			private static final long serialVersionUID = -4315390241296210531L;

			@Override
			protected final void onBeforeRender(){
				//setEnabled(enabled)
				if (!StringUtils.isEmpty(path) && parentPanel.isPasteable()){
					setEnabled(true);
				} else {
					setEnabled(false);
				}
                super.onBeforeRender();
            }

			@Override
            public void onClick(AjaxRequestTarget target) {
				parentPanel.pasteNode(target);
            	refresh("");
            }
        };
        paste.setOutputMarkupId(true);
        add(paste);

        //Copy Link
        final Component copy = new IndicatingAjaxLink<Object>("copyNode"){

			private static final long serialVersionUID = -1062211579369743790L;

			@Override
			protected final void onBeforeRender(){
				if (!StringUtils.isEmpty(path) && parentPanel.isCopyable()){
					setEnabled(true);
				} else {
					setEnabled(false);
				}
                super.onBeforeRender();
            }

			@Override
            public void onClick(AjaxRequestTarget target) {
				parentPanel.copyNode();
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

//			if (getContentService().isContentNode(path)){
//			contentText = getContentService().getContent(path).getText();
//		} else if (getContentService().isBlogNode(path)){
//			IBlogEntryData blogEntry = getContentService().getBlogEntry(path);
//			StringBuffer sb = new StringBuffer("Title : ").append(blogEntry.getTitle()).append("<br/>")
//				.append("Date : ").append(blogEntry.getDateTime()).append("<br/>").append("<br/>")
//				.append(blogEntry.getText());
//			contentText = sb.toString();
//		}else if (getContentService().isImageNode(path)){
//			contentText = "Image Node";
//		} else {
//			Item item = getAdminService().getItem(path);
//			contentText = item.toString();
//		}
		contentDisplay.modelChanged();
		//paste.

	}

    private static String displayNode(NodeDisplay display){
    	StringBuffer sb = new StringBuffer("Node Name : " + display.getName()).append("<br/>");
    	sb.append("Class Name : " + display.getClassName()).append("<br/>");
    	sb.append("<span style='text-decoration:underline;'>Properties</span><br/>");
    	Iterator<Map.Entry<String, NodeDisplay.NodePropertyDisplay>> iter = display.getProperties().entrySet().iterator();
    	Map.Entry<String, NodeDisplay.NodePropertyDisplay> text = null; // keep text for last
    	while (iter.hasNext()){
    		Map.Entry<String, NodeDisplay.NodePropertyDisplay> entry = iter.next();
    		if (entry.getKey().equals("text")){
    			text = entry;
    			continue;
    		}
			sb.append(displayNodeProperty(entry));
    	}
    	if (null != text){
    		sb.append(displayNodeProperty(text));
    	}

		return sb.toString();

    }

    private static String displayNodeProperty(Map.Entry<String, NodeDisplay.NodePropertyDisplay> entry){
    	StringBuffer sb = new StringBuffer();
    	sb.append(entry.getKey()+"[" + entry.getValue().type + "] : ");
    	if (!entry.getValue().type.equals("Binary")){
		    sb.append(entry.getValue().value);
    	}
		sb.append("<br/>");
    	return sb.toString();
    }

	protected IDataService getContentService() {
		return ((IDataServiceProvider) getApplication()).getRepositoryService();
	}

	protected IRepositoryAdminService getAdminService() {
		return ((IRepositoryAdminServiceProvider) getApplication()).getRepositoryAdminService();
	}

}
