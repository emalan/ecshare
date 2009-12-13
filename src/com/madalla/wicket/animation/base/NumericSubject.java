package com.madalla.wicket.animation.base;


public class NumericSubject extends AbstractSubject {
	private static final long serialVersionUID = 1L;
	
	private static final String TEMPLATE = ".addSubject(new NumericalStyleSubject(wicketGet('${subject}'), '${property}', ${from}, ${to}))";
	private double from;
	private double to;
	
	public NumericSubject(String subject, String property, double from, double d){
		super(subject, property);
		this.from = from;
		this.to = d;
	}
	
	@Override
	public String toString(){
		return render();
	}

	@Override
	String render() {
		return TEMPLATE.replace("${subject}", subject).replace("${property}", property).replace("${from}", Double.toString(from)).replace("${to}", Double.toString(to));
	}
}
