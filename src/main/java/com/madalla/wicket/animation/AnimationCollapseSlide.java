package com.madalla.wicket.animation;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;

/**
 * Convenience combined animation that reduces the size of an element, will set height using javascript prior to
 * collapsing.
 * 
 * @author Eugene Malan
 *
 */
public class AnimationCollapseSlide extends AnimationEventBehavior {
	private static final long serialVersionUID = 1L;

	private final Component target;
	private final int height;
	private final String unit;

	public AnimationCollapseSlide(Component target, int height, String unit) {
		this("onclick", target, height, unit);
	}

	public AnimationCollapseSlide(String event, Component target, int height, String unit) {
		super(event);
		this.target = target;
		this.height = height;
		this.unit = unit;
		target.setOutputMarkupId(true);
	}

	@Override
	protected void addAnimatorSubjects(IAnimator animator) {
		animator.addSubject(AnimatorSubject.discrete(target.getMarkupId(), "display", "none","", 0.1));
		animator.addSubject(AnimatorSubject.numeric(target.getMarkupId(), "height", 1, height, unit));
	}

	@Override
	protected String onEventAnimatorActions(IAnimatorActions animator) {
		return animator.toggle();
	}

	@Override
	protected IAjaxCallDecorator getAjaxCallDecorator() {
		// TODO Auto-generated method stub
		return new IAjaxCallDecorator() {
			
			private static final long serialVersionUID = 1L;

			public CharSequence decorateScript(Component component, CharSequence script) {
				// TODO Auto-generated method stub
				return null;
			}

			public CharSequence decorateOnSuccessScript(Component component, CharSequence script) {
				// TODO Auto-generated method stub
				return null;
			}

			public CharSequence decorateOnFailureScript(Component component, CharSequence script) {
				// TODO Auto-generated method stub
				return null;
			}

		};
	}
	
	

}
