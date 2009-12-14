package com.madalla.wicket.animation;

import com.madalla.wicket.animation.base.Animator;
import com.madalla.wicket.animation.base.NumericSubject;

public class AnimationPulsate extends AbstractAnimationEventBehavior{
	private static final long serialVersionUID = 1L;

	public AnimationPulsate(String event) {
		super(event);
	}

	@Override
	protected void addAnimatorSubjects(Animator animator) {
		animator.addSubject(new NumericSubject(getComponent().getMarkupId(),"opacity", 1, 0.25));		
	}

	@Override
	protected String onEventAnimatorActions(Animator animator) {
		return animator.play() + animator.reverse();
	}

}
