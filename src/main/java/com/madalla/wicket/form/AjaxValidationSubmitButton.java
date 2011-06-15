package com.madalla.wicket.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Add to form to enable Ajax submit of form. Use for forms only validate when button
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
	private static final Log log = LogFactory.getLog(AjaxValidationSubmitButton.class);

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
                    target.addComponent(component);
                }
            }

        };
        if (feedbackPanel != null){
            target.addComponent(feedbackPanel);
        }
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
                } else if (component.isValid()){
                    target.addComponent(component);
                    log.debug("Ajax onError - Component is valid. "+component);
                }

            }

        };
        if (feedbackPanel != null){
            target.addComponent(feedbackPanel);
        }

    }

    private abstract class AjaxValidationTemplate {

        private final Log log = LogFactory.getLog(AjaxValidationTemplate.class);

        public AjaxValidationTemplate(AjaxRequestTarget target, Form<?> form){
            processEvent(target, form);
        }

        abstract void eventAction(final AjaxRequestTarget target, FormComponent<?> component);

        private void processEvent(final AjaxRequestTarget target, Form<?> form){
            form.visitChildren(new Component.IVisitor<Component>() {
                public Object component(Component component){
                    log.debug("formVisitor="+component);
                    if ( component instanceof FormComponent<?>) {
                        FormComponent<?> formComponent = (FormComponent<?>) component;
                        eventAction(target, formComponent);
                    } else if (component instanceof FeedbackPanel){
                    	target.addComponent(component);
                    }
                    return null;
                }
            });
        }



    }


}
