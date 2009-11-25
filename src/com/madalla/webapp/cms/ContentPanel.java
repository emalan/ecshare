package com.madalla.webapp.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.Model;

import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.PageData;
import com.madalla.webapp.AdminPage;
import com.madalla.webapp.login.aware.LinkLoginAware;
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
    private String nodeName;
    private String nodeId;
    /**
     * 
     * @param id - wicket id
     * @param name - storage name
     * @param node - parent node of Content
     * @param returnPage
     */
    public ContentPanel(String id, String name, String node){
    	this(id, name, node, new Label("nested","stub").setVisible(false));
    }
    /**
     * 
     * @param id - wicket id
     * @param name - storage name
     * @param node - parent node of Content
     * @param returnPage
     */
    public ContentPanel(String id, String name, String node, Component nested){
        super(id);
        this.nodeName = node;
        this.nodeId = name;
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
        add(nested);

    }
    /**
     * 
     * @param id - wicket id and name
     * @param node - parent node of Content
     */
    public ContentPanel(String id, String node) {
        this(id, id, node);
    }

    public class ContentContainer extends WebMarkupContainer {
        private static final long serialVersionUID = 1L;

        public ContentContainer(String id, final String contentId, final String contentNode, final String contentBody) {
            super(id);

            final Model<String> contentModel = new Model<String>(contentBody);
            
            // add content
            Component label = new Label("contentBody", contentModel){
				private static final long serialVersionUID = 6930776696843471636L;

				@Override
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
            Link<Object> link = new LinkLoginAware("contentLink", ContentEditPage.class, false){
				private static final long serialVersionUID = 1L;

				@Override
				protected AdminPage constructAdminPage(Class<? extends AdminPage> clazz) {
					return new ContentEditPage(contentId, contentNode);
				}

            };
            link.setBeforeDisabledLink("");
            link.setAfterDisabledLink("");
            add(link);

        }
    }

}
