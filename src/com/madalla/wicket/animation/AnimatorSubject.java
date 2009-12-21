package com.madalla.wicket.animation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public abstract class AnimatorSubject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final String TEMPLATE_COLOR = ".addSubject(new ColorStyleSubject(wicketGet('${subject}'), '${property}', \"${from}\", \"${to}\"))";
	private static final String TEMPLATE_DISCRETE = ".addSubject(new DiscreteStyleSubject(wicketGet('${subject}'), '${property}', \"${from}\", \"${to}\", ${when}))";
	private static final String TEMPLATE_NUMERIC = ".addSubject(new NumericalStyleSubject(wicketGet('${subject}'), '${property}', ${from}, ${to},'${unit}'))";
	
	public static final AnimatorSubject discrete(final String subject, final String property, final String from, final String to){
		return discrete(subject, property, from, to, 0.5);
	}

	public static final AnimatorSubject discrete(final String subject, final String property, final String from, final String to, final double when){
		return new AnimatorSubject(subject, property){
			private static final long serialVersionUID = 1L;

			@Override
			String render() {
				return TEMPLATE_DISCRETE.replace("${subject}", subject)
				.replace("${property}", property)
				.replace("${from}", from)
				.replace("${to}", to)
				.replace("${when}", Double.toString(when));
			}
			
		};
	}
	
	public static final AnimatorSubject numeric(final String subject, final String property, final double from, final double to){
		return numeric(subject, property, from, to, "px");
	}
	
	public static final AnimatorSubject numeric(final String subject, final String property, final double from, final double to, final String unit){
		return new AnimatorSubject(subject, property){
			private static final long serialVersionUID = 1L;

			@Override
			String render() {
				return TEMPLATE_NUMERIC.replace("${subject}", subject)
				.replace("${property}", property)
				.replace("${from}", Double.toString(from))
				.replace("${to}", Double.toString(to))
				.replace("${unit}", unit);
			}
			
		};
	}
	
	public static final AnimatorSubject color(final String subject, final String property, final String from, final String to){
		return new AnimatorSubject(subject, property){
			private static final long serialVersionUID = 1L;

			@Override
			String render() {
				return TEMPLATE_COLOR.replace("${subject}", subject)
				.replace("${property}", property)
				.replace("${from}", from)
				.replace("${to}", to);
			}
			
		};
	}
	
	public static final List<AnimatorSubject> slideOpen(String subject, int size){
		List<AnimatorSubject> list = new ArrayList<AnimatorSubject>(3);
		list.add(AnimatorSubject.discrete(subject, "display", "none","", 0.1));
		list.add(AnimatorSubject.numeric(subject,"opacity", 0.0, 1.0));
		list.add(AnimatorSubject.numeric(subject, "height", 1, size,"em"));
		return list;
	}
	
	protected final String subject;
	protected final String property;
	
	public AnimatorSubject(String subject, String property){
		this.subject = subject;
		this.property = property;
	}
	
	abstract String render();
	
	@Override
	public String toString(){
		return render();
	}
}
