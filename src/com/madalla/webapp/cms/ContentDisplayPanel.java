package com.madalla.webapp.cms;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.madalla.service.cms.IContentData;
import com.madalla.service.cms.IContentService;
import com.madalla.service.cms.IContentServiceProvider;

public class ContentDisplayPanel extends Panel implements IContentData{

	private static final long serialVersionUID = 1L;

	private Log log = LogFactory.getLog(this.getClass());
	private Component nodePath;
	private Component delete;
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
                    setVisible(false);
                } else {
                    setVisible(true);
                }
                super.onBeforeRender();
            }
            
            public void onClick() {
            	IContentService service = getContentService();
            	service.deleteBlogEntry(path);
            	path = "";
                setResponsePage(getPage().getClass());
            }
        };
        delete.setOutputMarkupId(true);
        add(delete);
	}
	
	public Component getComponent(){
		return nodePath;
	}
	
	public void refresh(String path){
		log.debug("refresh - path="+path);
		nodePath.modelChanging();
		this.path = path;
		nodePath.modelChanged();
		delete.setVisible(true);
	}
	
	protected IContentService getContentService() {
		return ((IContentServiceProvider) getApplication()).getContentService();
	}
    
}
