package com.madalla.wicket.animation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Get your DiscreteStyleSubject, NumericalStyleSubject or ColorStyleSubject here.
 *
 * Use the following methods: discrete, numeric or color to create your own
 * DiscreteStyleSubject, NumericalStyleSubject or ColorStyleSubject
 *
 * Other static methods are provided for pre-configured or complex Subjects
 *
 * NOTE: make sure the styles you are animating are not overridden by another style in a
 * style sheet. Set your beginning style in the element.
 *
 * @author Eugene Malan
 *
 */
public abstract class AnimatorSubject implements Serializable{
	private static final long serialVersionUID = 1L;

	private static final String TEMPLATE_COLOR = ".addSubject(new ColorStyleSubject(jQuery('#${target}'), '${property}', \"${from}\", \"${to}\"))";
	private static final String TEMPLATE_DISCRETE = ".addSubject(new DiscreteStyleSubject(jQuery('#${target}'), '${property}', \"${from}\", \"${to}\", ${when}))";
	private static final String TEMPLATE_NUMERIC = ".addSubject(new NumericalStyleSubject(jQuery('#${target}'), '${property}', ${from}, ${to},'${unit}'))";

	public static final AnimatorSubject discrete(final String target, final String property, final String from, final String to){
		return discrete(target, property, from, to, 0.5);
	}

	public static final AnimatorSubject discrete(final String target, final String property, final String from, final String to, final double when){
		return new AnimatorSubject(target, property){
			private static final long serialVersionUID = 1L;

			@Override
			String render() {
				return TEMPLATE_DISCRETE.replace("${target}", target)
				.replace("${property}", property)
				.replace("${from}", from)
				.replace("${to}", to)
				.replace("${when}", Double.toString(when));
			}

		};
	}

	public static final AnimatorSubject numeric(final String target, final String property, final double from, final double to){
		return numeric(target, property, from, to, "px");
	}

	public static final AnimatorSubject numeric(final String target, final String property, final double from, final double to, final String unit){
		return new AnimatorSubject(target, property){
			private static final long serialVersionUID = 1L;

			@Override
			String render() {
				return TEMPLATE_NUMERIC.replace("${target}", target)
				.replace("${property}", property)
				.replace("${from}", Double.toString(from))
				.replace("${to}", Double.toString(to))
				.replace("${unit}", unit);
			}

		};
	}

	public static final AnimatorSubject color(final String target, final String property, final String from, final String to){
		return new AnimatorSubject(target, property){
			private static final long serialVersionUID = 1L;

			@Override
			String render() {
				return TEMPLATE_COLOR.replace("${target}", target)
				.replace("${property}", property)
				.replace("${from}", from)
				.replace("${to}", to);
			}

		};
	}

	public static final List<AnimatorSubject> slideOpen(String target, int size){
		return slideOpen(target, size, "em");
	}

	/**
	 * Creates a visually attractive Slide Open of a Block.
	 *
	 * NOTE: set style of target to : overflow: hidden; width: 100%;
	 *
	 * @param target - html element the subject will target
	 * @param size - size to open block to
	 * @param unit - px, em
	 * @return  List<AnimatorSubject>
	 */
	public static final List<AnimatorSubject> slideOpen(String target, int size, String unit){
		List<AnimatorSubject> list = new ArrayList<AnimatorSubject>(3);
		list.add(AnimatorSubject.discrete(target, "display", "none","", 0.1));
		list.add(AnimatorSubject.numeric(target,"opacity", 0.0, 1.0));
		list.add(AnimatorSubject.numeric(target, "height", 1, size, unit));
		return list;
	}

	public static final List<AnimatorSubject> fade(String target, int opacitytarget){
		List<AnimatorSubject> list = new ArrayList<AnimatorSubject>(1);
		list.add(AnimatorSubject.numeric(target, "opacity", 1.0, opacitytarget));
		return list;
	}

	protected final String target;
	protected final String property;

	public AnimatorSubject(String target, String property){
		this.target = target;
		this.property = property;
	}

	abstract String render();

	@Override
	public String toString(){
		return render();
	}
}
