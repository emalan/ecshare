package com.madalla.wicket.animation.base;


public class ColorSubject extends AbstractSubject {
	private static final long serialVersionUID = 1L;

	private static final String TEMPLATE = ".addSubject(new ColorStyleSubject(wicketGet('${subject}'), '${property}', \"${from}\", \"${to}\"))";
	
	private final String from;
	private final String to;
	
	public ColorSubject(String subject, String property, String from, String to){
		super(subject, property);
		this.from = from;
		this.to = to;
	}

	@Override
	String render() {
		return TEMPLATE.replace("${subject}", subject).replace("${property}", property).replace("${from}", from).replace("${to}", to);
	}
}
