package com.madalla.wicket.animation;


import junit.framework.TestCase;

public class AnimationTest  extends TestCase {

	public void testAnimator(){
		
		{
			Animator animator = new Animator(1000);
		
			animator.addSubject(AnimatorSubject.numeric("form1", "height", 150, 5));
			animator.addSubject(AnimatorSubject.discrete("form1", "display","","none", 0.95));
			animator.addSubject(AnimatorSubject.color("form1", "display","#FFFFFF","#F4C"));

			System.out.println(animator);
		}
		
		{
			Animator animator = new Animator(1000);
		
			//animator.addSubjects(AnimatorSubject.slideShow("form1", 150));

			System.out.println(animator);
		}
		
	}
}
