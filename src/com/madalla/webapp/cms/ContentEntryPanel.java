package com.madalla.webapp.cms;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import tiny_mce.TinyMce;

import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.ContentEntryData;
import com.madalla.bo.page.PageData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.webapp.CmsSession;
import com.madalla.wicket.form.AjaxValidationStyleSubmitButton;

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
    private static final CompressedResourceReference JAVASCRIPT_ADM = new CompressedResourceReference(
            ContentEntryPanel.class, "ContentEntryPanel_admin.js");
    private static final CompressedResourceReference JAVASCRIPT = new CompressedResourceReference(
            ContentEntryPanel.class, "ContentEntryPanel.js");
    private static final CompressedResourceReference JAVASCRIPT_SPR = new CompressedResourceReference(
            ContentEntryPanel.class, "ContentEntryPanel_super.js");
    Log log = LogFactory.getLog(this.getClass());

    private ContentEntryData contentEntry;

    final class ContentForm extends Form<ContentEntryData> {
        private static final long serialVersionUID = -3526743712542402160L;

        public ContentForm(final String name, IModel<ContentEntryData> model) {
            super(name, model);
            add(new TextArea<String>("text"));
        }

    }

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
        if (((CmsSession)getSession()).isSuperAdmin()){
        	add( JavascriptPackageResource.getHeaderContribution(JAVASCRIPT_SPR));
        } else if (((CmsSession)getSession()).isCmsAdminMode()) {
        	add( JavascriptPackageResource.getHeaderContribution(JAVASCRIPT_ADM));
        } else {
        	add( JavascriptPackageResource.getHeaderContribution(JAVASCRIPT));
        }

        PageData page = getRepositoryservice().getPage(nodeName);
        ContentData content = getRepositoryservice().getContent(page, contentId);
        contentEntry = getRepositoryservice().getContentEntry(content, getSession().getLocale());
        
        Form<ContentEntryData> form = new ContentForm("contentForm", new CompoundPropertyModel<ContentEntryData>(contentEntry));
        
        final FeedbackPanel feedback = new ComponentFeedbackPanel("feedback", form);
        feedback.setOutputMarkupId(true);
        form.add(feedback);
        
        AjaxButton submitLink = new AjaxValidationStyleSubmitButton("submitButton", form) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form) {
                super.onError(target, form);
            }

            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                super.onSubmit(target, form);
                log.debug("Submiting populated Content object to Content service.");
                getRepositoryservice().saveContentEntry((ContentEntryData)form.getModelObject());
                log.debug("Content successfully saved to repository. content=" + contentEntry);
                form.info(getString("message.success"));
            }

			@Override
			protected String getOnClickScript() {
				return "tinyMCE.get('text').save();";
			}
            
        };
        submitLink.setOutputMarkupId(true);
        form.add(submitLink);
        add(form);
    }

    
    private IRepositoryService getRepositoryservice(){
    	return ((IRepositoryServiceProvider) getApplication()).getRepositoryService();
    }


}
