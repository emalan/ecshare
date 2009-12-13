package com.madalla.wicket.animation;

import java.io.Serializable;


public abstract class AbstractSubject implements Serializable{
	private static final long serialVersionUID = 1L;
	
	protected final String subject;
	protected final String property;
	
	public AbstractSubject(String subject, String property){
		this.subject = subject;
		this.property = property;
	}
	
	abstract String render();
	
	@Override
	public String toString(){
		return render();
	}
}
