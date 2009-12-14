package com.madalla.wicket.animation;

import org.apache.wicket.Component;

import com.madalla.wicket.animation.base.Animator;
import com.madalla.wicket.animation.base.DiscreteSubject;
import com.madalla.wicket.animation.base.NumericSubject;


/**
 * @author Eugene Malan
 *
 */
public class AnimationOpenSlide extends AbstractAnimationEventBehavior {
	private static final long serialVersionUID = 1L;
	
	private final Component subject;
	private final int height;

	public AnimationOpenSlide(String event, Component subject, int height) {
		super(event);
		this.subject = subject;
		this.height = height;
		subject.setOutputMarkupId(true);
	}
	
	@Override
	protected void addAnimatorSubjects(Animator animator) {
		animator.addSubject(new DiscreteSubject(subject.getMarkupId(), "display", "none","", 0.1));
		animator.addSubject(new NumericSubject(subject.getMarkupId(), "height", 1, height));
		
	}

	@Override
	protected String onEventAnimatorActions(Animator animator) {
		return animator.toggle();
	}

}
