package com.madalla.wicket.form;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;
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

		target.addComponent(getFormComponent());

		getFormComponent().info("Valid");
		//refreshForm(getFormComponent().getForm(), target);
		if (feedbackPanel != null){
			target.addComponent(feedbackPanel);
			target.appendJavascript(
				"$('"+feedbackPanel.getMarkupId()+"').parentNode.addClassName('"+validClass+"');"+
				"$('"+feedbackPanel.getMarkupId()+"').parentNode.removeClassName('"+invalidClass+"');");
		}
	}

	@Override
	protected void onError(AjaxRequestTarget target, RuntimeException e) {
		animation(target);
		log.debug("onError -" + getFormComponent());

		target.addComponent(getFormComponent());

		if (feedbackPanel != null){
			target.addComponent(feedbackPanel);
			target.appendJavascript(
                "$('"+feedbackPanel.getMarkupId()+"').parentNode.addClassName('"+invalidClass+"');"+
                "$('"+feedbackPanel.getMarkupId()+"').parentNode.removeClassName('"+validClass+"');");
		}
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.renderJavascriptReference(Scriptaculous.PROTOTYPE);//TODO make sure this needs prototype ???
		animator.setUniqueId(getComponent().getMarkupId());
		animator.addSubject(AnimatorSubject.numeric(getComponent().getMarkupId(),"opacity", 1, 0.25));
		animator.renderHead(response);
	}

	//animation to indicate to user that validation occured
	private void animation(AjaxRequestTarget target) {
		target.appendJavascript(animator.play()+animator.reverse());
	}

}
