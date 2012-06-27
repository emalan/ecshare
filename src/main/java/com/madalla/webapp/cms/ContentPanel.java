package com.madalla.webapp.cms;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.madalla.webapp.admin.pages.AdminPageLink;
import com.madalla.webapp.admin.pages.ContentEditPage;
import com.madalla.webapp.css.Css;

/**
 * Panel to display content from Repository.
 * <p>
 * Panel links to edit page when valid user is logged in.
 * </p>
 * 
 * @author Eugene Malan
 * 
 */
public class ContentPanel extends Panel {
    private static final long serialVersionUID = 1L;

    /**
     * 
     * @param id
     *            - wicket id and name
     * @param model
     *            - parent node of Content
     */
    public ContentPanel(final String id, final IModel<String> model, final PageParameters params) {
        this(id, id, model, params);
    }

    /**
     * 
     * @param id
     *            - wicket id
     * @param name
     *            - storage name
     * @param node
     *            - parent node of Content, typically Page Name
     */
    public ContentPanel(String id, String name, final IModel<String> model, final PageParameters params) {
        this(id, model, params, new Label("nested", "stub").setVisible(false));
    }

    /**
     * 
     * @param id
     *            - wicket id
     * @param name
     *            - storage name
     * @param node
     *            - parent node of Content
     * @param nested
     *            - to support nesting of another component in the content panel
     */
    public ContentPanel(final String id, final IModel<String> model, final PageParameters params, final Component nested) {
        super(id, model);

        Component contentBlock = new ContentContainer("contentBlock", model, params);
        contentBlock.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = -3131361470864509715L;

            @Override
            public String getObject() {
                String cssClass;
                if (((IContentAdmin) getSession()).isLoggedIn()) {
                    cssClass = "contentEdit textBlock edit";
                } else {
                    cssClass = "contentBlock textBlock";
                }
                return cssClass;
            }
        }));
        add(contentBlock);
        add(nested);

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        super.renderHead(response);
        response.render(CssHeaderItem.forReference(Css.CSS_ICON));
    }

    public class ContentContainer extends WebMarkupContainer {
        private static final long serialVersionUID = 1L;

        public ContentContainer(final String id, final IModel<String> contentModel, final PageParameters params) {
            super(id);

            // add content
            Component label = new Label("contentBody", contentModel);
            label.setEscapeModelStrings(false);
            add(label);

            // add link to edit it
            add(new AdminPageLink("editLink", ContentEditPage.class, params));

        }
    }

}
