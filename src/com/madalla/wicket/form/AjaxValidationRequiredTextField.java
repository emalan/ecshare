package com.madalla.wicket.form;

import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

public class AjaxValidationRequiredTextField extends RequiredTextField<String> implements IAjaxValidationComponent{
	private static final long serialVersionUID = 1L;
	
    public AjaxValidationRequiredTextField(final String id) {
        super(id);
        add(new AjaxValidationBehaviour());
    }

    public AjaxValidationRequiredTextField(final String id, FeedbackPanel feedback) {
        super(id);
        feedback.setFilter(new ComponentFeedbackMessageFilter(this));
        add(new AjaxValidationBehaviour(feedback));
    }

	public AjaxValidationRequiredTextField(final String id, final IModel<String> model) {
		super(id, model);
		add(new AjaxValidationBehaviour());
	}
	
	public AjaxValidationRequiredTextField(final String id, final IModel<String> model, FeedbackPanel feedback) {
		super(id, model);
		feedback.setFilter(new ComponentFeedbackMessageFilter(this));
		add(new AjaxValidationBehaviour(feedback));
	}


}
