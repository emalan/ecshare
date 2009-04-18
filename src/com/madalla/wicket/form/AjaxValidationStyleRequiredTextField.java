package com.madalla.wicket.form;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

public class AjaxValidationStyleRequiredTextField extends ValidationStyleRequiredTextField implements ValidationStyleComponent{
	private static final long serialVersionUID = 1L;
	

    public AjaxValidationStyleRequiredTextField(final String id) {
        super(id);
        add(new AjaxValidationBehaviour());
    }

    public AjaxValidationStyleRequiredTextField(final String id, FeedbackPanel feedback) {
        super(id, feedback);
        add(new AjaxValidationBehaviour(feedback));
    }

	public AjaxValidationStyleRequiredTextField(final String id, final IModel<String> model) {
		super(id, model);
		add(new AjaxValidationBehaviour());
	}
	
	public AjaxValidationStyleRequiredTextField(final String id, final IModel<String> model, FeedbackPanel feedback) {
		super(id, model, feedback);
		add(new AjaxValidationBehaviour(feedback));
	}


}
