package com.madalla.webapp.cms;

import static com.madalla.webapp.PageParams.RETURN_PAGE;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.PropertyModel;

import com.madalla.bo.ContentData;
import com.madalla.bo.IPageData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.pages.ContentAdminPage;
import com.madalla.webapp.scripts.tiny_mce.TinyMce;

/**
 * The Wicket application must implement the IContentServiceProvider interface
 * 
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
	public ContentEntryPanel(String name, final String nodeName, final String contentId, Class<? extends Page> returnPage) {
        super(name);
        this.contentPage = returnPage;
        add(HeaderContributor.forJavaScript(TinyMce.class, "tiny_mce.js"));
        add(HeaderContributor.forJavaScript(JAVASCRIPT));

        add(new PageLink("returnLink", returnPage));
		add(new BookmarkablePageLink("contentAdminLink", ContentAdminPage.class, 
				new PageParameters(RETURN_PAGE + "=" + returnPage.getName())){
			private static final long serialVersionUID = 1801145612969874170L;

            protected final void onBeforeRender() {
                if (((IContentAdmin)getSession()).isCmsAdminMode()) {
                    setEnabled(true);
                } else {
                    setEnabled(false);
                }
                super.onBeforeRender();
            }
		});

        IPageData page = getRepositoryservice().getPage(nodeName);
        content = getRepositoryservice().getContent(page, contentId, getSession().getLocale());
        add(new ContentForm("contentForm"));
    }

    final class ContentForm extends Form {
        private static final long serialVersionUID = -3526743712542402160L;

        public ContentForm(final String name) {
            super(name);
            //content.setText(text);
            add(new TextArea("text", new PropertyModel(this, "text")));
            add(new FeedbackPanel("feedback"));

            add(new Link("cancelButton") {
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
