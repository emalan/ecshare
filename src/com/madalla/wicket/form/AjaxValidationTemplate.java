package com.madalla.wicket.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public abstract class AjaxValidationTemplate {
    
    private static final Log log = LogFactory.getLog(AjaxValidationTemplate.class);
    
    public AjaxValidationTemplate(AjaxRequestTarget target, Form<?> form){
        processEvent(target, form);
    }
    
    abstract void eventAction(final AjaxRequestTarget target, FormComponent<?> component);
    

    private void processEvent(final AjaxRequestTarget target, Form<?> form){
        form.visitChildren(new Component.IVisitor<Component>() {
            public Object component(Component component){
                log.debug("formVisitor="+component);
                if (component instanceof ValidationStyleComponent && component instanceof FormComponent<?>) {
                    FormComponent<?> formComponent = (FormComponent<?>) component;
                    eventAction(target, formComponent);
                } else if (component instanceof FeedbackPanel){
                    log.debug("processEvent - adding feedback to target.");
                    FeedbackPanel feedback = (FeedbackPanel) component;
                    target.addComponent(feedback);
                }
                return null;
            }
        });
    }
    
    
    
}
