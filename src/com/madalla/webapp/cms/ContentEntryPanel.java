package com.madalla.webapp.cms;

import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.PropertyModel;

import com.madalla.service.cms.IContentService;
import com.madalla.webapp.scripts.tiny_mce.TinyMce;

public class ContentEntryPanel extends Panel implements IContentAware {
    private static final long serialVersionUID = 1L;
    private static final CompressedResourceReference JAVASCRIPT = 
        new CompressedResourceReference(ContentEntryPanel.class, "ContentEntryPanel.js");
    Log log = LogFactory.getLog(this.getClass());
    IContentService service;
    private final Content content = new Content();

    public ContentEntryPanel(String name, final PageParameters parameters, IContentService service) {
        super(name);
        this.service = service;
        add(HeaderContributor.forJavaScript(TinyMce.class,"tiny_mce.js"));
        add(HeaderContributor.forJavaScript(JAVASCRIPT));
        content.setClassName(parameters.getString(CONTENT_CLASS));
        content.setContentId(parameters.getString(CONTENT_ID));
        add(new ContentForm("contentForm"));
    }
    
    protected void setContent(Content content) throws RepositoryException{
        service.setContent(content);
    }
    
    final class ContentForm extends Form {
        private static final long serialVersionUID = -3526743712542402160L;

        public ContentForm(final String name) {
            super(name);
            String text = service.getContentData(content.getClassName(), content.getContentId());
            content.setText(text);
            add(new TextArea("text", new PropertyModel(content, "text")));
            add(new FeedbackPanel("feedback"));
        }

        public void onSubmit() {
            log.debug("Submiting populated Content object to Content service. content="
                      + content);
            try {
                setContent(content);
            } catch (RepositoryException e) {
                info("There was a problem saving content. " + e.getMessage());
                log.error("Exception while saving content to repository.", e);
            }
            info("Content saved to repository");
            log.info("Content successfully saved to repository. content="
                    + content);
        }
    }



}
