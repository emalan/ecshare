package com.madalla.wicket.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;

public abstract class AjaxValidationStyleSubmitButton extends IndicatingAjaxButton{

	private static final long serialVersionUID = 5857888282264283281L;
	private static final Log log = LogFactory.getLog(AjaxValidationStyleSubmitButton.class);
	
	public AjaxValidationStyleSubmitButton(String id, Form<Object> form) {
		super(id, form);
	}

    @Override
	protected void onSubmit(final AjaxRequestTarget target, Form<?> form){
        log.debug("Ajax onSubmit called.");
        new AjaxValidationTemplate(target, form){

            @Override
            void eventAction(AjaxRequestTarget target, FormComponent<?> component) {
                if (component.isValid() ){
                    target.addComponent(component);
                }
            }
            
        };
	}
	
    @Override
	protected void onError(final AjaxRequestTarget target, Form<?> form){
        log.debug("Ajax onError called");
        new AjaxValidationTemplate(target, form){

            @Override
            void eventAction(AjaxRequestTarget target, FormComponent<?> component) {
                if (!component.isValid() ){
                    target.addComponent(component);
                    log.debug("Ajax onError - Component is invalid. Component MarkupId="+component.getMarkupId()+". Message is " +component.getFeedbackMessage().getMessage());
                }
                
            }
            
        };
    }
	


}
