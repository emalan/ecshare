package com.madalla.wicket.animation;

import junit.framework.TestCase;

public class AnimationTest  extends TestCase {

	public void testAnimator(){
		
		Animator animator = new Animator(1000);
		
		animator.addSubject(new NumericSubject("form1", "height", 150, 5));
		animator.addSubject(new DiscreteSubject("form1", "display","","none", 0.95));

		System.out.println(animator);
	}
}
