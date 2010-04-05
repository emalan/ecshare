package com.madalla.webapp.cms.editor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.ComponentFeedbackPanel;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

import com.madalla.bo.page.ContentEntryData;
import com.madalla.webapp.panel.CmsPanel;
import com.madalla.wicket.form.AjaxValidationSubmitButton;

public class ContentFormPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(this.getClass());
	
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
        
        final AjaxValidationSubmitButton submitLink = new AjaxValidationSubmitButton("submitButton", form, feedback) {

			private static final long serialVersionUID = 1L;

            @Override
			protected void onBeforeRender() {
				super.onBeforeRender();
			}

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				
				log.debug("Submiting populated Content object to Content service. " + form.getModelObject());
				saveData((ContentEntryData)form.getModelObject());
                target.addComponent(feedback);
                form.info(getString("message.success"));
			}

			@Override
			protected String getOnClickScript() {

				String message = getString("message.editor.fail");
				String text = "<ul class=\"feedbackPanel\"><li class=\"feedbackPanelINFO\"><span class=\"feedbackPanelWARN\">"+message+"</span></li></ul>";
				String error = "Wicket.$('"+feedback.getMarkupId()+"').innerHTML = '"+text+"';";
				
				return "var ed = tinyMCE.get('text'); if (ed.isDirty()){ ed.save(); ed.isNotDirty = 1;} else {"+error+"return false;}";

			}
            
        };
        submitLink.setOutputMarkupId(true);

        form.add(submitLink);

        add(form);

	}	
	
	public void changeContentEntry(ContentEntryData contentEntryData){
		if (contentEntryData == null) {
			throw new WicketRuntimeException("Construction error");
		}
		this.contentModel = contentEntryData;
	}

}
