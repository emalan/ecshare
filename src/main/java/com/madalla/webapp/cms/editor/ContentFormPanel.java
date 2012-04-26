package com.madalla.webapp.cms.editor;

import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tiny_mce.TinyMceSetup;
import wicket.contrib.tinymce.ajax.TinyMceAjaxButton;

import com.madalla.bo.page.ContentEntryData;
import com.madalla.webapp.CmsPanel;

public class ContentFormPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private ContentEntryData contentModel;

    final class ContentForm extends Form<ContentEntryData> {
        private static final long serialVersionUID = -3526743712542402160L;

        public ContentForm(final String name, IModel<ContentEntryData> model) {
            super(name, model);
            TextArea<String> text = new TextArea<String>("text", new IModel<String>(){
				private static final long serialVersionUID = 1L;

				public String getObject() {
					return contentModel.getText();
				}

				public void setObject(String object) {
					contentModel.setText(object);

				}

				public void detach() {

				}

            });
            text.add(TinyMceSetup.createBehavior(getAppSession().getLocale(), getAppSession()));
            add(text);
        }
    }

	public ContentFormPanel(String name, ContentEntryData contentEntryData){
		super(name);
		if (contentEntryData == null){
			throw new WicketRuntimeException("Constructor Exception - contentEntry data must have a value.");
		}
		this.contentModel = contentEntryData;
        final Form<ContentEntryData> form = new ContentForm("contentForm", new IModel<ContentEntryData>(){

					private static final long serialVersionUID = 1L;

					public ContentEntryData getObject() {
						return contentModel ;
					}

					public void setObject(ContentEntryData object) {
						contentModel = object;

					}

					public void detach() {
						//contentModel = null;
					}

        });

        final FeedbackPanel feedback = new ComponentFeedbackPanel("feedback", form);
        feedback.setOutputMarkupId(true);
        form.add(feedback);
        
        final TinyMceAjaxButton submit = new TinyMceAjaxButton("submitButton", form) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				log.debug("Submiting populated Content object to Content service. " + form.getModelObject());
				saveData((ContentEntryData)form.getModelObject());
                target.add(feedback);
                form.info(getString("message.success"));
				
			}
			
			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				log.error("Error while submitting Content. " + form.getModelObject());
				form.error("");
			}
		};

        form.add(submit);

        add(form);

	}

	public void changeContentEntry(ContentEntryData contentEntryData){
		if (contentEntryData == null) {
			throw new WicketRuntimeException("Construction error");
		}
		this.contentModel = contentEntryData;
	}

}
