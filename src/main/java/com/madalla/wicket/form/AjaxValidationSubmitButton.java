package com.madalla.wicket.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;

/**
 * Add to form to enable Ajax submit of form. Use for forms that validate when button
 * is clicked. For Components that self-validate (AjaxValidationBehaviour), rather use
 * AjaxValidationForm. {@link AjaxValidationForm}
 *
 * Make sure to add setoutputmarkup to true and to add ValidationBehaviour if wanted.
 *
 *
 *
 * @author Eugene Malan
 *
 */
public abstract class AjaxValidationSubmitButton extends IndicatingAjaxButton{

	private static final long serialVersionUID = 5857888282264283281L;
	private static final Logger log = LoggerFactory.getLogger(AjaxValidationSubmitButton.class);

	private final FeedbackPanel feedbackPanel;

	public AjaxValidationSubmitButton(String id, Form<?> form, FeedbackPanel feedbackPanel) {
	    super(id, form);
	    this.feedbackPanel = feedbackPanel;
	    if (feedbackPanel != null){
	    	feedbackPanel.setOutputMarkupId(true);
	    }
	}
	public AjaxValidationSubmitButton(String id, Form<?> form) {
        this(id, form, null);
    }

    @Override
	protected void onSubmit(final AjaxRequestTarget target, Form<?> form){
        log.debug("Ajax onSubmit called.");
        new AjaxValidationTemplate(target, form){

            @Override
            void eventAction(AjaxRequestTarget target, FormComponent<?> component) {
                if (component.isValid() && component.getOutputMarkupId()){
                    target.add(component);
                }
            }

        };
        if (feedbackPanel != null){
            target.add(feedbackPanel);
        }
	}

    @Override
	protected void onError(final AjaxRequestTarget target, Form<?> form){
        log.debug("Ajax onError called");
        new AjaxValidationTemplate(target, form){

            @Override
            void eventAction(AjaxRequestTarget target, FormComponent<?> component) {
                if (!component.isValid() ){
                    target.add(component);
                    log.debug("Ajax onError - Component is invalid. Component MarkupId="+component.getMarkupId()+". Message is " +component.getFeedbackMessage().getMessage());
                } else if (component.isValid()){
                    target.add(component);
                    log.debug("Ajax onError - Component is valid. "+component);
                }

            }

        };
        if (feedbackPanel != null){
            target.add(feedbackPanel);
        }

    }

    private abstract class AjaxValidationTemplate {

        private final Logger log = LoggerFactory.getLogger(AjaxValidationTemplate.class);

        public AjaxValidationTemplate(AjaxRequestTarget target, Form<?> form){
            processEvent(target, form);
        }

        abstract void eventAction(final AjaxRequestTarget target, FormComponent<?> component);

        private void processEvent(final AjaxRequestTarget target, Form<?> form){
            form.visitChildren(new IVisitor<Component, Void>() {

				public void component(Component component, IVisit<Void> visit) {
					log.debug("formVisitor=" + component);
                    if (component instanceof FormComponent<?>) {
                        FormComponent<?> formComponent = (FormComponent<?>) component;
                        eventAction(target, formComponent);
                    } else if (component instanceof FeedbackPanel){
                    	target.add(component);
                    }
					
				}
            });
        }



    }


}
