package com.madalla.webapp.cms;

import javax.jcr.RepositoryException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
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
 * @author exmalan
 *
 */
public class ContentEntryPanel extends Panel implements IContentAware {
    private static final long serialVersionUID = 1L;
    private static final CompressedResourceReference JAVASCRIPT = 
        new CompressedResourceReference(ContentEntryPanel.class, "ContentEntryPanel.js");
    Log log = LogFactory.getLog(this.getClass());
    private final Content content = new Content();
    private Page contentPage;

    public ContentEntryPanel(String name, Page contentPage, String id, IContentService service) {
        super(name);
        this.contentPage = contentPage;
        add(HeaderContributor.forJavaScript(TinyMce.class,"tiny_mce.js"));
        add(HeaderContributor.forJavaScript(JAVASCRIPT));
        content.setClassName(contentPage.getClass().getName());
        content.setContentId(id);
        String text = service.getContentData(content.getClassName(), content.getContentId());
        add(new ContentForm("contentForm", text));
    }
    
    final class ContentForm extends Form {
        private static final long serialVersionUID = -3526743712542402160L;
        
        public ContentForm(final String name, String text) {
            super(name);
            content.setText(text);
            add(new TextArea("text", new PropertyModel(content, "text")));
            add(new FeedbackPanel("feedback"));
            
            Button cancelButton = new Button("cancelButton"){
				private static final long serialVersionUID = 1L;

				public void onSubmit() {
					
					setResponsePage(contentPage);
					
				}
            };
            cancelButton.setDefaultFormProcessing(false);
            add(cancelButton);
        }

        public void onSubmit() {
            log.debug("Submiting populated Content object to Content service. content="
                      + content);
            try {
            	IContentService service = ((IContentServiceProvider)getPage().getApplication()).getContentService();
                service.setContent(content);
                info("Content saved to repository");
                log.info("Content successfully saved to repository. content="
                        + content);
                contentPage.render();
                setResponsePage(contentPage);
            } catch (RepositoryException e) {
                info("There was a problem saving content. " + e.getMessage());
                log.error("Exception while saving content to repository.", e);
            }
        }
        
        
    }



}
