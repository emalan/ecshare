package com.madalla.wicket.form;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.wicket.animation.Animator;
import com.madalla.wicket.animation.AnimatorSubject;

/**
 * Self Updating Validation Behaviour.
 *
 * Validation happens onBlur and there is an animation to indicate to user that something is happening.
 * If feedback is supplied then the enclosing element is set to a valid/invalid class, so that you can style
 * a response to the validation.
 *
 * @author Eugene Malan
 *
 */
public class AjaxValidationBehaviour extends AjaxFormComponentUpdatingBehavior {
	private static final long serialVersionUID = 1L;

	private Logger log = LoggerFactory.getLogger(this.getClass());
	private final FeedbackPanel feedbackPanel;

	private final Animator animator;

	private String validClass ;
	private String invalidClass ;

	public AjaxValidationBehaviour() {
		this(null);
	}
	public AjaxValidationBehaviour(String validClass, String invalidClass){
		this(null, validClass, invalidClass);
	}
	public AjaxValidationBehaviour(FeedbackPanel feedbackPanel) {
		this(feedbackPanel,"inputValid","inputError");
	}
	public AjaxValidationBehaviour(FeedbackPanel feedbackPanel, String validClass, String invalidClass) {
		super("onblur");
		animator = new Animator();
		if (feedbackPanel != null){
			feedbackPanel.setOutputMarkupId(true);
		}
		this.feedbackPanel = feedbackPanel;
		this.validClass = validClass;
		this.invalidClass = invalidClass;
	}

	public void reset(AjaxRequestTarget target){
		log.debug("reset - "+getFormComponent());
	}

	@Override
	protected void onUpdate(AjaxRequestTarget target) {
		animation(target);
		log.debug("onUpdate - "+getFormComponent());

		target.add(getFormComponent());

		getFormComponent().info("Valid");
		//refreshForm(getFormComponent().getForm(), target);
		if (feedbackPanel != null){
			target.add(feedbackPanel);
			target.appendJavaScript(
				"$('"+feedbackPanel.getMarkupId()+"').parent().addClass('"+validClass+"');"+
				"$('"+feedbackPanel.getMarkupId()+"').parent().removeClass('"+invalidClass+"');");
		}
	}

	@Override
	protected void onError(AjaxRequestTarget target, RuntimeException e) {
		animation(target);
		log.debug("onError -" + getFormComponent());

		target.add(getFormComponent());

		if (feedbackPanel != null){
			target.add(feedbackPanel);
			target.appendJavaScript(
                "$('#"+feedbackPanel.getMarkupId()+"').parent().addClass('"+invalidClass+"');"+
                "$('#"+feedbackPanel.getMarkupId()+"').parent().removeClass('"+validClass+"');");
		}
	}
	
	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		animator.setUniqueId(getComponent().getMarkupId());
		animator.addSubject(AnimatorSubject.numeric(getComponent().getMarkupId(),"opacity", 1, 0.25));
		animator.renderHead(response);
	}

	//animation to indicate to user that validation occured
	private void animation(AjaxRequestTarget target) {
		target.appendJavaScript(animator.play()+animator.reverse());
	}

}
