package com.madalla.wicket.animation;

import java.util.List;

public interface IAnimator {

	IAnimator addSubject(AnimatorSubject subject);

	IAnimator addSubjects(List<AnimatorSubject> subjects);

}
