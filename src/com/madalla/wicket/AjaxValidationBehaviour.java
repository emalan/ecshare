package com.madalla.wicket;

import static com.madalla.webapp.scripts.scriptaculous.Scriptaculous.PROTOTYPE;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class AjaxValidationBehaviour extends AjaxFormComponentUpdatingBehavior {
	private static final long serialVersionUID = 1L;
	
	private final FeedbackPanel feedbackPanel;
	
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
		this.feedbackPanel = feedbackPanel;
		this.validClass = validClass;
		this.invalidClass = invalidClass;
	}

	@Override
	protected void onUpdate(AjaxRequestTarget target) {
		target.addComponent(getFormComponent());
		if (feedbackPanel != null){
			target.addComponent(feedbackPanel);
			target.appendJavascript(
				"$('"+feedbackPanel.getMarkupId()+"').parentNode.addClassName('"+validClass+"');"+
				"$('"+feedbackPanel.getMarkupId()+"').parentNode.removeClassName('"+invalidClass+"');"+
				"$('"+feedbackPanel.getMarkupId()+"').update('Valid')");
		}
	}

	@Override
	protected void onError(AjaxRequestTarget target, RuntimeException e) {
		target.addComponent(getFormComponent());
		target.appendJavascript(
                "new Effect.Pulsate($('" + getFormComponent().getMarkupId() + "'),{pulses:1, duration:0.3});");
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
		response.renderCSSReference(PROTOTYPE);
	}

}
