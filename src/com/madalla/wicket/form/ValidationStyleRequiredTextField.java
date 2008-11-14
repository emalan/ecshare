package com.madalla.wicket.form;

import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

public class ValidationStyleRequiredTextField extends RequiredTextField {
	private static final long serialVersionUID = 1L;

	public ValidationStyleRequiredTextField(final String id, final IModel model, FeedbackPanel feedback) {
		super(id, model);
		feedback.setOutputMarkupId(true);
		feedback.setFilter(new ComponentFeedbackMessageFilter(this));
		add(new ValidationStyleBehaviour());
		add(new AjaxValidationBehaviour(feedback));
		
	}


}
