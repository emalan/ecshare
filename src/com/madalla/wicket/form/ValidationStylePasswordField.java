package com.madalla.wicket.form;

import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

public class ValidationStylePasswordField extends PasswordTextField implements ValidationStyleComponent{
	private static final long serialVersionUID = 3384438368054434609L;

	public ValidationStylePasswordField(final String id, final IModel model, FeedbackPanel feedback){
        super(id, model);
        feedback.setOutputMarkupId(true);
        feedback.setFilter(new ComponentFeedbackMessageFilter(this));
        add(new ValidationStyleBehaviour());
        setOutputMarkupId(true);
    }
}
