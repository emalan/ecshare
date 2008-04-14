package com.madalla.webapp.cms;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;

import com.madalla.service.cms.IContentService;

public class ContentPanel extends Panel implements IContentAware {
    private static final long serialVersionUID = 1L;
    private Page contentEditPage;

    /**
     * 
     * @param id
     * @param contentEditPage
     * @param contentService
     * @param contentAdmin
     */
    public ContentPanel(String id, String className , Page contentEditPage, IContentService contentService, final IContentAdmin contentAdmin) {
        super(id);
        this.contentEditPage = contentEditPage;
        String contentBody = contentService.getContentData(className, id);
        Component contentBlock = new ContentContainer("contentBlock", id,contentBody, contentAdmin);
        contentBlock.add(new AttributeModifier("class", new AbstractReadOnlyModel() {
            public Object getObject() {
                String cssClass;
                if (contentAdmin.isCmsAdminMode()) {
                    cssClass = "contentEdit";
                } else {
                    cssClass = "contentBlock";
                }
                return cssClass;
            }
        }));
        add(contentBlock);
    }
    
    public class ContentContainer extends WebMarkupContainer{
        private static final long serialVersionUID = 1L;
        public ContentContainer(String id, String contentId, String contentBody, final IContentAdmin contentAdmin){
            super(id);
            
            //add content
            Component label = new Label("contentBody",contentBody).setEscapeModelStrings(false);
            add(label);
            
            //add link to edit it
            Link link = new Link("contentLink"){
                private static final long serialVersionUID = 1801145612969874170L;

                protected final void onBeforeRender(){
                    if (contentAdmin.isCmsAdminMode()){
                        setEnabled(true);
                    } else {
                        setEnabled(false);
                    }
                    super.onBeforeRender();
                }
				public void onClick() {
					setResponsePage(contentEditPage);
				}
                
            };
            link.setBeforeDisabledLink("");
            link.setAfterDisabledLink("");
            add(link);

        }
        protected void onBeforeRender() {
            super.onBeforeRender();
        }
    }

}
