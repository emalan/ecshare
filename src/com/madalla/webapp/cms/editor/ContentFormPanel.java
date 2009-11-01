package com.madalla.webapp.cms.editor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import com.madalla.bo.page.ContentEntryData;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.wicket.form.AjaxValidationStyleSubmitButton;

public class ContentFormPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(this.getClass());
	
    final class ContentForm extends Form<ContentEntryData> {
        private static final long serialVersionUID = -3526743712542402160L;

        public ContentForm(final String name, IModel<ContentEntryData> model) {
            super(name, model);
            add(new TextArea<String>("text", new PropertyModel<String>(model.getObject(), "text")));
        }
    }
    
	public ContentFormPanel(String name, IModel<ContentEntryData> model){
		super(name);

        final Form<ContentEntryData> form = new ContentForm("contentForm", model);
        
        final FeedbackPanel feedback = new ComponentFeedbackPanel("feedback", form);
        feedback.setOutputMarkupId(true);
        form.add(feedback);
        
        final AjaxValidationStyleSubmitButton submitLink = new AjaxValidationStyleSubmitButton("submitButton", form, feedback) {

			private static final long serialVersionUID = 1L;

            @Override
			protected void onBeforeRender() {
            	//setEnabled(false);
				super.onBeforeRender();
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				log.debug("Submiting populated Content object to Content service. " + form.getModelObject());
                getRepositoryService().saveContentEntry((ContentEntryData)form.getModelObject());
                target.addComponent(feedback);
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

}
