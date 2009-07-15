package com.madalla.webapp.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.PropertyModel;

import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.PageData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.scripts.tiny_mce.TinyMce;

/**
 * Content Entry Panel - Edit User Content using a WYSWYG HTML editor.
 * <p>
 * Panel uses the TinyMCE (Javascript WYSIWYG Editor).
 * Note: The Wicket application must implement the IContentServiceProvider 
 * interface.
 * </p>
 * @author Eugene Malan
 * 
 */
public class ContentEntryPanel extends Panel {
    private static final long serialVersionUID = 1L;
    private static final CompressedResourceReference JAVASCRIPT = new CompressedResourceReference(
            ContentEntryPanel.class, "ContentEntryPanel.js");
    Log log = LogFactory.getLog(this.getClass());

    private Class<? extends Page> contentPage;
    private ContentData content;

	/**
	 * @param name - wicket id
	 * @param nodeName
	 * @param contentId
	 * @param returnPage - used to create return Link
	 * 
	 */
	public ContentEntryPanel(String name, final String nodeName, final String contentId) {
        super(name);
        add( JavascriptPackageResource.getHeaderContribution(TinyMce.class, "tiny_mce.js"));
        add( JavascriptPackageResource.getHeaderContribution(JAVASCRIPT));

        PageData page = getRepositoryservice().getPage(nodeName);
        content = getRepositoryservice().getContent(page, contentId, getSession().getLocale());
        add(new ContentForm("contentForm"));
    }

    final class ContentForm extends Form<Object> {
        private static final long serialVersionUID = -3526743712542402160L;

        public ContentForm(final String name) {
            super(name);
            //content.setText(text);
            add(new TextArea<String>("text", new PropertyModel<String>(this, "text")));
            add(new FeedbackPanel("feedback"));

            add(new Link<Object>("cancelButton") {
                private static final long serialVersionUID = 1L;

				@Override
				public void onClick() {
					setResponsePage(contentPage);
					
				}
            });
            
            add(new SubmitLink("submitButton"));
            
        }
        public String getText() {
    		return content.getText();
    	}
        public void setText(String val){
        	content.setText(val);
        }
        public void onSubmit() {
            log.debug("Submiting populated Content object to Content service.");
            getRepositoryservice().saveContent(content);
            info("Content saved to repository");
            log.debug("Content successfully saved to repository. content=" + content);
            setResponsePage(contentPage);
        }

    }
    
    private IRepositoryService getRepositoryservice(){
    	return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
    }


}
