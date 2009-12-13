package com.madalla.wicket.animation.base;



public class DiscreteSubject extends AbstractSubject {
	private static final long serialVersionUID = 1L;
	
	private static final String TEMPLATE = ".addSubject(new DiscreteStyleSubject(wicketGet('${subject}'), '${property}', \"${from}\", \"${to}\", ${when}))";
	
	private final String from;
	private final String to;
	private final double when;
	
	public DiscreteSubject(String subject, String property, String from, String to){
	    this(subject, property, from, to, 0.5);	
	}
	
	public DiscreteSubject(String subject, String property, String from, String to, double when){
		super(subject, property);
		this.from = from;
		this.to = to;
		this.when = when;
	}
	
	public String render(){
		return TEMPLATE.replace("${subject}", subject).replace("${property}", property).replace("${from}", from).replace("${to}", to).replace("${when}", Double.toString(when));
	}

}
