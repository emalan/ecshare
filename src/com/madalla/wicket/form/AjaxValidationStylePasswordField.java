package com.madalla.wicket.form;

import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

public class AjaxValidationStylePasswordField extends ValidationStylePasswordField implements ValidationStyleComponent{

    private static final long serialVersionUID = -1566218353018783473L;

    public AjaxValidationStylePasswordField(final String id, final IModel model, FeedbackPanel feedback){
        super(id, model, feedback);
        add(new AjaxValidationBehaviour(feedback));
    }
}
