package com.madalla.webapp.cms.editor;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;

import com.madalla.bo.page.ContentData;
import com.madalla.bo.page.ContentEntryData;
import com.madalla.bo.page.PageData;
import com.madalla.service.IRepositoryService;
import com.madalla.service.IRepositoryServiceProvider;
import com.madalla.wicket.form.AjaxValidationStyleSubmitButton;

public class ContentFormPanel extends Panel{
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(this.getClass());
	private ContentEntryData contentEntry;
	
    final class ContentForm extends Form<ContentEntryData> {
        private static final long serialVersionUID = -3526743712542402160L;

        public ContentForm(final String name, IModel<ContentEntryData> model) {
            super(name, model);
            add(new TextArea<String>("text"));
        }
    }
    
	public ContentFormPanel(String name, final String nodeName, final String contentId, final Locale locale){
		super(name);
		
        PageData page = getRepositoryservice().getPage(nodeName);
        ContentData content = getRepositoryservice().getContent(page, contentId);
        contentEntry = getRepositoryservice().getContentEntry(content, locale);

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
