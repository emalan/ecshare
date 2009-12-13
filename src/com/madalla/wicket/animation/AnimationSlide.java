package com.madalla.wicket.animation;

import org.apache.wicket.Component;


/**
 * @author Eugene Malan
 *
 */
public class AnimationSlide extends AbstractAnimationBehavior {
	private static final long serialVersionUID = 1L;
	
	private final Component subject;

	public AnimationSlide(String event, Component subject) {
		super(event);
		this.subject = subject;
	}
	
	@Override
	protected void addAnimatorSubjects(Animator animator) {
		animator.addSubject(new NumericSubject(subject.getMarkupId(), "height", 150, 5));
		animator.addSubject(new DiscreteSubject(subject.getMarkupId(), "display", "","none", 0.95));
	}

	@Override
	protected String onEventAnimatorActions(Animator animator) {
		return animator.toggle();
	}

}
