package com.madalla.wicket.form;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IFormVisitorParticipant;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;

/**
 * Extend this to add Ajax submit behaviour to your form.
 * <p>
 * NOTE : You will need to add a <em>formSubmit</em> and a <em>formFeedback</em> to
 * your markup for the form.
 * </p>
 * <p>
 * The Form will give you an Ajax submit button as well as enabling inputs and feedbacks
 * to work better with Ajax. 
 * </p>
 * 
 * @author Eugene Malan
 *
 * @param <T>
 */
public abstract class AjaxValidationForm<T> extends Form<T>  {


	private static final long serialVersionUID = 1L;
	
	private Log log = LogFactory.getLog(this.getClass());
	
	public AjaxValidationForm(String id) {
		super(id);
		init();
	}

	public AjaxValidationForm(String id, IModel<T> model) {
		super(id, model);
		init();
	}
	
	protected FeedbackPanel addFeedbackPanel(){
		IFeedbackMessageFilter filter = new ContainerFeedbackMessageFilter(this);
		final FeedbackPanel formFeedback = new FeedbackPanel("formFeedback", filter);
		 formFeedback.setMaxMessages(1);
		add(formFeedback);
		return formFeedback;
	}
	
	private void init(){

		final FeedbackPanel formFeedback = addFeedbackPanel();

		add(new IndicatingAjaxButton("formSubmit", this){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(final AjaxRequestTarget target, Form<?> form) {
				
				form.visitFormComponents(new FormComponent.IVisitor(){

					public Object formComponent(IFormVisitorParticipant component) {
						FormComponent<?> formComponent = (FormComponent<?>) component;
						log.debug("onSubmit.formVisiter - " + formComponent + formComponent.getModelObject());
						
						if (formComponent.isValid() && formComponent.getOutputMarkupId()){
							target.addComponent(formComponent);
						}
						return null;
					}
					
				});

				target.addComponent(formFeedback);
				AjaxValidationForm.this.onSubmit(target);
				
			}

			
			@Override
			protected void onError(final AjaxRequestTarget target, Form<?> form) {
				log.debug("Ajax onError called");

				form.visitFormComponents(new FormComponent.IVisitor() {

					public Object formComponent(IFormVisitorParticipant component) {
						FormComponent<?> formComponent = (FormComponent<?>) component;
						log.debug("onSubmit.formVisiter - " + formComponent);

						if (!formComponent.isValid() && formComponent.getOutputMarkupId()) {
							target.addComponent(formComponent);
						}
						return null;
					}

				});
				target.addComponent(formFeedback);
			}
		});
	}

	@Override
	protected void onBeforeRender() {
		
		
		this.visitChildren(new Component.IVisitor<Component>(){

			public Object component(Component component) {
				log.debug("form visitChildren - "+component);
				if (component instanceof IAjaxValidationComponent && component instanceof FormComponent<?>) {
					log.debug("adding validation style behaviour. " + component);
					component.add(new ValidationStyleBehaviour());
					component.setOutputMarkupId(true);
                } else if (component instanceof FeedbackPanel){
                    log.debug("keep reference to this feedbackPanel." + component);
                    component.setOutputMarkupId(true);
                }
				return null;
			}
			
		});

		super.onBeforeRender();
	}
	
	protected abstract void onSubmit(final AjaxRequestTarget target) ;
	

	

}
