package com.madalla.wicket.form;

import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

public class ValidationStyleRequiredTextField extends RequiredTextField<String> implements
        ValidationStyleComponent {

    private static final long serialVersionUID = 1L;

    public ValidationStyleRequiredTextField(final String id) {
        super(id);
        add(new ValidationStyleBehaviour());
    }
    
    public ValidationStyleRequiredTextField(final String id, FeedbackPanel feedback) {
        super(id);
        feedback.setOutputMarkupId(true);
        feedback.setFilter(new ComponentFeedbackMessageFilter(this));
        add(new ValidationStyleBehaviour());
    }

    public ValidationStyleRequiredTextField(final String id, final IModel<String> model) {
        super(id, model);
        add(new ValidationStyleBehaviour());
    }

    public ValidationStyleRequiredTextField(final String id, final IModel<String> model,
            FeedbackPanel feedback) {
        super(id, model);
        feedback.setOutputMarkupId(true);
        feedback.setFilter(new ComponentFeedbackMessageFilter(this));
        add(new ValidationStyleBehaviour());
    }

}
