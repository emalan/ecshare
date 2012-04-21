package com.madalla.wicket.animation;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;

/**
 * Use to attach animation to component event.
 *
 * @author Eugene Malan
 *
 */
public abstract class AnimationEventBehavior extends AjaxEventBehavior  {
	private static final long serialVersionUID = 1L;

	private final Animator animator;

	public AnimationEventBehavior(String event, int duration) {
		super(event);
		animator = new Animator(duration);
	}

	public AnimationEventBehavior(String event) {
		super(event);
		animator = new Animator();
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		super.renderHead(component, response);
		animator.setUniqueId(getComponent().getMarkupId());
		addAnimatorSubjects(animator);
		animator.renderHead(response);
	}

	@Override
	protected void onEvent(AjaxRequestTarget target) {
		target.appendJavaScript(onEventAnimatorActions(animator));
	}

	abstract protected String onEventAnimatorActions(IAnimatorActions animator);

	abstract protected void addAnimatorSubjects(IAnimator animator);

}
