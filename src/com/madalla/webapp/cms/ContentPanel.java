package com.madalla.webapp.cms;

import static com.madalla.webapp.PageParams.RETURN_PAGE;
import static com.madalla.webapp.cms.ContentParameters.CONTENT_ID;
import static com.madalla.webapp.cms.ContentParameters.CONTENT_NODE;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;

import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.PageData;
import com.madalla.webapp.pages.ContentEditPage;
import com.madalla.webapp.panel.CmsPanel;

/**
 * Panel to display content from Repository.
 * <p>
 * Panel links to edit page when valid user is logged in.
 * </p>
 * @author Eugene Malan
 *
 */
public class ContentPanel extends CmsPanel {
    private static final long serialVersionUID = 1L;
    private Log log = LogFactory.getLog(this.getClass());
    private Class<? extends Page> returnPage;
    private String nodeName;
    private String nodeId;

    /**
     * 
     * @param id - wicket id
     * @param name - storage name
     * @param node - parent node of Content
     * @param returnPage
     */
    public ContentPanel(String id, String name, String node, Class<? extends Page> returnPage){
        super(id);
        this.nodeName = node;
        this.nodeId = name;
        this.returnPage = returnPage;
        log.debug("Content Panel being created for node=" + node + " id=" + id);
        PageData page = getRepositoryService().getPage(node);
        ContentData content = getRepositoryService().getContent(page, nodeId);
        String contentBody = getRepositoryService().getContentText(content, getSession().getLocale());
        Component contentBlock = new ContentContainer("contentBlock", nodeId, node, contentBody);
        contentBlock.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = -3131361470864509715L;

            public String getObject() {
                String cssClass;
                if (((IContentAdmin)getSession()).isLoggedIn()) {
                    cssClass = "contentEdit";
                } else {
                    cssClass = "contentBlock";
                }
                return cssClass;
            }
        }));
        add(contentBlock);
    }
    /**
     * 
     * @param id - wicket id and name
     * @param node - parent node of Content
     * @param returnPage - Return Page duh!
     */
    public ContentPanel(String id, String node, Class<? extends Page> returnPage) {
        this(id, id, node, returnPage);
    }

    public class ContentContainer extends WebMarkupContainer {
        private static final long serialVersionUID = 1L;

        public ContentContainer(String id, String contentId, String contentNode, String contentBody) {
            super(id);

            final Model<String> contentModel = new Model<String>(contentBody);
            
            // add content
            Component label = new Label("contentBody", contentModel){
				private static final long serialVersionUID = 6930776696843471636L;

				protected void onBeforeRender(){
                    PageData page = getRepositoryService().getPage(nodeName);
                    ContentData content = getRepositoryService().getContent(page, nodeId);
                    String contentBody = getRepositoryService().getContentText(content, getSession().getLocale());
                    log.debug("onBeforeRender - setting new Content.");
                    contentModel.setObject(contentBody);
                    super.onBeforeRender();
                }
            };
            label.setEscapeModelStrings(false);
            add(label);

            // add link to edit it
            Link<Page> link = new BookmarkablePageLink<Page>("contentLink", ContentEditPage.class, new PageParameters(CONTENT_NODE + "="
                    + contentNode + "," + CONTENT_ID + "=" + contentId + "," + RETURN_PAGE + "="
                    + returnPage.getName())) {
                private static final long serialVersionUID = 1801145612969874170L;

                protected final void onBeforeRender() {
                    if (((IContentAdmin)getSession()).isLoggedIn()) {
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
    }

}
