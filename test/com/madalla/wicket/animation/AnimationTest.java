package com.madalla.wicket.animation;

import com.madalla.wicket.animation.base.Animator;
import com.madalla.wicket.animation.base.ColorSubject;
import com.madalla.wicket.animation.base.DiscreteSubject;
import com.madalla.wicket.animation.base.NumericSubject;

import junit.framework.TestCase;

public class AnimationTest  extends TestCase {

	public void testAnimator(){
		
		Animator animator = new Animator(1000);
		
		animator.addSubject(new NumericSubject("form1", "height", 150, 5));
		animator.addSubject(new DiscreteSubject("form1", "display","","none", 0.95));
		animator.addSubject(new ColorSubject("form1", "display","#FFFFFF","#F4C"));

		System.out.println(animator);
	}
}
