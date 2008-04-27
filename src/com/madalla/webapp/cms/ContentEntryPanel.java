package com.madalla.webapp.cms;

import java.util.Locale;

import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.PropertyModel;

import com.madalla.service.cms.IContentService;
import com.madalla.service.cms.IContentServiceProvider;
import com.madalla.webapp.scripts.tiny_mce.TinyMce;

/**
 * The Wicket application must implement the IContentServiceProvider interface
 * 
 * @author exmalan
 * 
 */
public class ContentEntryPanel extends Panel implements IContentAware {
    private static final long serialVersionUID = 1L;
    private static final CompressedResourceReference JAVASCRIPT = new CompressedResourceReference(
            ContentEntryPanel.class, "ContentEntryPanel.js");
    Log log = LogFactory.getLog(this.getClass());
    private final Content content = new Content();
    private Class contentPage;

    public ContentEntryPanel(String name, final PageParameters parameters) {
        super(name);
        String nodeName = parameters.getString(CONTENT_NODE);
        String id = parameters.getString(CONTENT_ID);
        String returnPage = parameters.getString(CONTENT_PAGE);
        try {
            this.contentPage = Class.forName(returnPage);
        } catch (ClassNotFoundException e) {
            log.error("constructor - Exception while getting return Class.", e);
        }
        add(HeaderContributor.forJavaScript(TinyMce.class, "tiny_mce.js"));
        add(HeaderContributor.forJavaScript(JAVASCRIPT));

        content.setClassName(nodeName);
        content.setContentId(id);

        IContentService service = ((IContentServiceProvider) getApplication()).getContentService();
        String text = service.getContentData(content.getClassName(), content.getContentId(), getSession().getLocale());
        add(new ContentForm("contentForm", text));
    }

    final class ContentForm extends Form {
        private static final long serialVersionUID = -3526743712542402160L;

        public ContentForm(final String name, String text) {
            super(name);
            content.setText(text);
            add(new TextArea("text", new PropertyModel(content, "text")));
            add(new FeedbackPanel("feedback"));

            Button cancelButton = new Button("cancelButton") {
                private static final long serialVersionUID = 1L;

                public void onSubmit() {
                    setResponsePage(contentPage);
                }
            };
            cancelButton.setDefaultFormProcessing(false);
            add(cancelButton);
        }

        public void onSubmit() {
            log.debug("Submiting populated Content object to Content service. content=" + content);
            try {
                IContentService service = ((IContentServiceProvider) getPage().getApplication()).getContentService();
                service.setContent(content, getSession().getLocale());
                info("Content saved to repository");
                log.info("Content successfully saved to repository. content=" + content);
                setResponsePage(contentPage);
            } catch (RepositoryException e) {
                info("There was a problem saving content. " + e.getMessage());
                log.error("Exception while saving content to repository.", e);
            }
        }

    }

}
