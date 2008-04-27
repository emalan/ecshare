package com.madalla.webapp.cms;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import com.madalla.service.cms.IContentServiceProvider;

public class ContentPanel extends Panel implements IContentAware {
    private static final long serialVersionUID = 1L;
    private Log log = LogFactory.getLog(this.getClass());
    private Class contentEditPage;
    private Class returnPage;

    /**
     * 
     * @param id
     * @param contentEditPage
     * @param contentService
     * @param contentAdmin
     */
    public ContentPanel(String id, String node, Class returnPage, Class contentEditPage,
            final IContentAdmin contentAdmin) {
        super(id);
        this.contentEditPage = contentEditPage;
        this.returnPage = returnPage;
        log.debug("Content Panel being created for node=" + node + " id=" + id);
        IContentService contentService = ((IContentServiceProvider) getApplication()).getContentService();
        Locale locale = getSession().getLocale();
        String contentBody = contentService.getContentData(node, contentService.getLocaleId(id, locale));
        Component contentBlock = new ContentContainer("contentBlock", id, node, contentBody, contentAdmin);
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

    public class ContentContainer extends WebMarkupContainer {
        private static final long serialVersionUID = 1L;

        public ContentContainer(String id, String contentId, String contentNode, String contentBody,
                final IContentAdmin contentAdmin) {
            super(id);

            // add content
            Component label = new Label("contentBody", contentBody).setEscapeModelStrings(false);
            add(label);

            // add link to edit it
            Link link = new BookmarkablePageLink("contentLink", contentEditPage, new PageParameters(CONTENT_NODE + "="
                    + contentNode + "," + CONTENT_ID + "=" + contentId + "," + CONTENT_PAGE + "="
                    + returnPage.getName())) {
                private static final long serialVersionUID = 1801145612969874170L;

                protected final void onBeforeRender() {
                    if (contentAdmin.isCmsAdminMode()) {
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
