package com.madalla.wicket.form;

import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

public class ValidationStylePasswordField extends PasswordTextField {

    private static final long serialVersionUID = -1566218353018783473L;

    public ValidationStylePasswordField(final String id, final IModel model, FeedbackPanel feedback){
        super(id, model);
        feedback.setOutputMarkupId(true);
        feedback.setFilter(new ComponentFeedbackMessageFilter(this));
        add(new ValidationStyleBehaviour());
        add(new AjaxValidationBehaviour(feedback));
        
    }
}
