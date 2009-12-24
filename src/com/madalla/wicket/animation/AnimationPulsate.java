package com.madalla.wicket.animation;

public class AnimationPulsate extends AnimationEventBehavior{
	private static final long serialVersionUID = 1L;

	public AnimationPulsate(String event) {
		super(event);
	}

	@Override
	protected void addAnimatorSubjects(IAnimator animator) {
		animator.addSubject(AnimatorSubject.numeric(getComponent().getMarkupId(),"opacity", 1, 0.25));
	}

	@Override
	protected String onEventAnimatorActions(Animator animator) {
		return animator.play() + animator.reverse();
	}

}
