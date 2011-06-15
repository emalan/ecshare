package com.madalla.wicket.animation;

/**
 * Methods to call to get Javascript that triggers animations.
 *
 * @author Eugene Malan
 *
 */
public interface IAnimatorActions {

	/**
	 * plays the animation from the start to the end
	 *
	 * @return Javascript to trigger animation
	 */
	String play();

	/**
	 * plays the animation in reverse
	 *
	 * @return Javascript to trigger animation
	 */
	String reverse();

	/**
	 * @return Javascript to trigger animation
	 */
	String toggle();

	/**
	 * plays the animation from it's current position to End
	 *
	 * @return Javascript to trigger animation
	 */
	String seekToEnd();

	/**
	 * plays the animation from it's current position to the Begin
	 *
	 * @return Javascript to trigger animation
	 */
	String seekToBegin();

	/**
	 * plays the animation from it's current position
	 *
	 * @param pos - position at which animation will end at 0.5 (half-way)
	 * @return Javascript to trigger animation
	 */
	String seekTo(double pos);

}
