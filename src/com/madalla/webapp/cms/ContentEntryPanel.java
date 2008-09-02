package com.madalla.webapp.cms;

import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.PropertyModel;

import com.madalla.service.cms.Content;
import com.madalla.service.cms.IContentService;
import com.madalla.service.cms.IContentServiceProvider;
import com.madalla.webapp.scripts.tiny_mce.TinyMce;

/**
 * The Wicket application must implement the IContentServiceProvider interface
 * 
 * @author Eugene Malan
 * 
 */
public class ContentEntryPanel extends Panel implements IContentAware {
    private static final long serialVersionUID = 1L;
    private static final CompressedResourceReference JAVASCRIPT = new CompressedResourceReference(
            ContentEntryPanel.class, "ContentEntryPanel.js");
    Log log = LogFactory.getLog(this.getClass());

    private Class<? extends Page> contentPage;
    private final String nodeName;
    private final String contentId;
    private String text;

	/**
	 * @param name
	 * @param parameters
	 * 
	 * TODO Get return Page from application
	 */
	public ContentEntryPanel(String name, final PageParameters parameters) {
        super(name);
        nodeName = parameters.getString(CONTENT_NODE);
        contentId = parameters.getString(CONTENT_ID);
        String returnPage = parameters.getString(CONTENT_PAGE);
        try {
            this.contentPage = (Class<? extends Page>) Class.forName(returnPage);
        } catch (ClassNotFoundException e) {
            log.error("constructor - Exception while getting return Class.", e);
        } catch (ClassCastException e) {
        	log.error("constructor - Exception while casting return Class.", e);
        }
        add(HeaderContributor.forJavaScript(TinyMce.class, "tiny_mce.js"));
        add(HeaderContributor.forJavaScript(JAVASCRIPT));

        IContentService service = ((IContentServiceProvider) getApplication()).getContentService();
        text = service.getContentData(nodeName, contentId, getSession().getLocale());
        add(new ContentForm("contentForm"));
    }

    final class ContentForm extends Form {
        private static final long serialVersionUID = -3526743712542402160L;

        public ContentForm(final String name) {
            super(name);
            //content.setText(text);
            add(new TextArea("text", new PropertyModel(this, "text")));
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
        public String getText() {
    		return text;
    	}
        public void setText(String val){
        	text = val;
        }
        public void onSubmit() {
            log.debug("Submiting populated Content object to Content service.");
            try {
                IContentService service = ((IContentServiceProvider) getPage().getApplication()).getContentService();
                Content content = new Content(nodeName, service.getLocaleId(contentId, getSession().getLocale()));
                content.setText(text);
                service.setContent(content);
                info("Content saved to repository");
                log.debug("Content successfully saved to repository. content=" + content);
                setResponsePage(contentPage);
            } catch (RepositoryException e) {
                info("There was a problem saving content. " + e.getMessage());
                log.error("Exception while saving content to repository.", e);
            }
        }

    }

}
