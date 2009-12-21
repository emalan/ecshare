package com.madalla.wicket.animation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;



public class Animator implements IAnimator, Serializable{
	private static final long serialVersionUID = 1L;

	private final static int DURATION = 400;
	private final static String PREFIX = "anim_";
	private static final String TEMPLATE = "${id} = new Animator({ duration: ${duration} })";
	
	private final int duration;
	private String id;

	private final List<AnimatorSubject> subjectList = new ArrayList<AnimatorSubject>();
	
	public Animator(int duration){
	    this.duration = duration;
	}
	
	public Animator() {
		this(DURATION);
	}

	public Animator addSubject(final AnimatorSubject subject){
		if (subject == null)
		{
			throw new AnimationRuntimeException("Argument may not be null");
		}
		subjectList.add(subject);
		return this;
	}
	
	public Animator addSubjects(final List<AnimatorSubject> subjects){
		if (subjects == null)
		{
			throw new AnimationRuntimeException("Argument may not be null");
		}
		subjectList.addAll(subjects);
		return this;
	}
	
	public String play(){
		return id + ".play();";
	}
	
	public String reverse(){
		return id + ".reverse();";
	}
	
	public String toggle(){
		return id + ".toggle();";
	}
	
	public String inspect(){
		return id + ".inspect();";
	}
	
	//console.log() only works in Firefox browser 
	@SuppressWarnings("unused")
	private String debug(){
		return "console.log("+id+".inspect());";
	}
	
	private String renderTemplate(){
		StringBuilder sb = new StringBuilder(TEMPLATE.replace("${duration}", Integer.toString(duration)));
		for (AnimatorSubject s: subjectList){
			sb.append(s.toString());
		}
		
		sb.append(";");
		return sb.toString();
	}
	
	public String render(){
		if (id == null || StringUtils.isEmpty(id)){
			throw new AnimationRuntimeException("Attempting to render animation while id is Null. " + renderTemplate());
		}
		return renderTemplate().replace("${id}", id);
	}

	@Override
	public String toString() {
		return renderTemplate();
	}

	public void setMarkupId(String id) {
		this.id = PREFIX + id;
		
	}
	
}
