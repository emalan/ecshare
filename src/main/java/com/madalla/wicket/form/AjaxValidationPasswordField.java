package com.madalla.wicket.form;

import org.apache.wicket.feedback.ComponentFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

public class AjaxValidationPasswordField extends PasswordTextField implements IAjaxValidationComponent{

    private static final long serialVersionUID = -1566218353018783473L;

    public AjaxValidationPasswordField(final String id, final IModel<String> model, FeedbackPanel feedback){
        super(id, model);
        feedback.setFilter(new ComponentFeedbackMessageFilter(this));
        add(new AjaxValidationBehaviour(feedback));
        setResetPassword(false);
    }
}
