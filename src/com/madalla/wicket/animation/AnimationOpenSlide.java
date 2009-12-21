package com.madalla.wicket.animation;

import org.apache.wicket.Component;



/**
 * @author Eugene Malan
 *
 */
public class AnimationOpenSlide extends AbstractAnimationEventBehavior {
	private static final long serialVersionUID = 1L;
	
	private final Component subject;
	private final int height;
	private final String unit;
	
	public AnimationOpenSlide(Component subject, int height, String unit) {
		this("onclick", subject, height, unit);
	}

	public AnimationOpenSlide(String event, Component subject, int height, String unit) {
		super(event);
		this.subject = subject;
		this.height = height;
		this.unit = unit;
		subject.setOutputMarkupId(true);
	}
	
	@Override
	protected void addAnimatorSubjects(IAnimator animator) {
		animator.addSubject(AnimatorSubject.discrete(subject.getMarkupId(), "display", "none","", 0.1));
		animator.addSubject(AnimatorSubject.numeric(subject.getMarkupId(), "height", 1, height, unit));
	}

	@Override
	protected String onEventAnimatorActions(Animator animator) {
		return animator.toggle();
	}

}
