package com.madalla.wicket.animation;

import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;

import com.madalla.webapp.scripts.JavascriptResources;

public abstract class AbstractAnimationEventBehavior extends AjaxEventBehavior  {
	private static final long serialVersionUID = 1L;

	private final Animator animator;
	
	public AbstractAnimationEventBehavior(String event, int duration) {
		super(event);
		animator = new Animator(duration);
	}
	
	public AbstractAnimationEventBehavior(String event) {
		super(event);
		animator = new Animator();
	}
	
	@Override
	protected void onBind() {
		super.onBind();
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		animator.setMarkupId(getComponent().getMarkupId());
		addAnimatorSubjects(animator);
		response.renderJavascriptReference(JavascriptResources.ANIMATOR);
		response.renderOnDomReadyJavascript(animator.render());
	}
	
	@Override
	protected void onEvent(AjaxRequestTarget target) {
		target.appendJavascript(onEventAnimatorActions(animator));	
	}
	
	abstract protected String onEventAnimatorActions(final Animator animator);
	
	abstract protected void addAnimatorSubjects(IAnimator animator);

}
