package com.madalla.webapp.cms;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;

import com.madalla.service.cms.IContentService;

public class ContentPanel extends Panel implements IContentAware {
    private static final long serialVersionUID = 1L;
    private String contentBody;

    public ContentPanel(String id, Class pageClass, Class contentPage,
            IContentService contentService, final IContentAdmin contentAdmin) {
        super(id);
        this.contentBody = contentService.getContentData(pageClass.getName(), id);
        Component contentBlock = new ContentContainer("contentBlock",pageClass, contentPage, id, contentAdmin);
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
        public ContentContainer(String id, Class pageClass, Class contentPage, String contentId, final IContentAdmin contentAdmin){
            super(id);
            
            //add content
            Component label = new Label("contentBody",contentBody).setEscapeModelStrings(false);
            add(label);
            
            //add link to edit it
            Map params = new HashMap();
            params.put(CONTENT_ID,contentId);
            params.put(CONTENT_CLASS,pageClass.getName());
            Link link = new BookmarkablePageLink("contentLink",contentPage ,new PageParameters(params)){
                private static final long serialVersionUID = 1801145612969874170L;

                protected final void onBeforeRender(){
                    if (contentAdmin.isCmsAdminMode()){
                        setEnabled(true);
                    } else {
                        setEnabled(false);
                    }
                    super.onBeforeRender();
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
