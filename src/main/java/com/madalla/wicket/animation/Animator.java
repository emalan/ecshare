package com.madalla.wicket.animation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.Behavior;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.head.OnDomReadyHeaderItem;
import org.apache.wicket.markup.html.IHeaderContributor;

import com.madalla.webapp.scripts.JavascriptResources;

/**
 * Main class - use this for more flexibility. Add to Page as HeaderContributor.
 *
 * You create an Animator and then add subjects for it to work on. If you only need to activate the
 * animation from one component, then it is better to use AnimationEventBehavior
 *
 * Here we create an Animator and add a subject to fade something
 * <pre>
 *    final Animator fade = new Animator().addSubject(AnimatorSubject.numeric(targetMarkupId,"opacity", 1.0, 0));
 *    add(fade)
 *
 *    //the animation is typically triggered in some Ajax Event, something like this...
 *    protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
 *        fade.play();
 *        ....
 *
 * </pre>
 *
 * Take a look at the AnimatorSubject class for more complex pre-done subjects, here is one that
 * will slide open a block set it to visible and change the opacity to give a visually pleasing effect.
 * <pre>
 *    final Animator hideShowSomething = new Animator().addSubjects(AnimatorSubject.slideOpen(targetMarkupId, 15, "em"));
 *    add(hideShowSomething);
 *
 *    // add this code to Ajax event
 *    hideShowSomething.toggle();
 *
 * </pre>
 *
 * @author Eugene Malan
 * @see AnimationEventBehavior
 */
public class Animator extends Behavior implements IAnimator, IAnimatorActions, IHeaderContributor, Serializable {
	private static final long serialVersionUID = 1L;

	private static final int DURATION = 400;
	private static final String SCRIPT_NAME = "AnimatorNamespace"; 
	private final static String PREFIX = "ANI.anim_";
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

	public Animator(String id){
		this();
	    setUniqueId(id);
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

	public String seekTo(double pos){
		return id + ".seekTo(" + pos + ");";
	}

	public String seekToBegin() {
		return seekTo(0.0);
	}

	public String seekToEnd() {
		return seekTo(1.0);
	}

	/**
	 * Useful for debugging
	 */
	public String javascriptInspect(){
		return id + ".inspect();";
	}

	//console.log() only works in Firefox browser with Firebug open
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

	private String render(){
		if (id == null || StringUtils.isEmpty(id)){
			setUniqueId(RandomStringUtils.randomAlphanumeric(5));
		}
		return renderTemplate().replace("${id}", id);
	}

	@Override
	public String toString() {
		return renderTemplate();
	}

	/**
	 * The id will be generated if not set, but this helps identify the animation.
	 *
	 * @param id
	 */
	public void setUniqueId(String id) {
		this.id = PREFIX + id;

	}

	@Override
	public void renderHead(Component component, IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(JavascriptResources.ANIMATOR));
		response.render(OnDomReadyHeaderItem.forScript(this.render()));
	}

	public void renderHead(IHeaderResponse response) {
		response.render(JavaScriptHeaderItem.forReference(JavascriptResources.ANIMATOR));
		response.render(JavaScriptHeaderItem.forScript("var ANI = {};", SCRIPT_NAME));
		response.render(OnDomReadyHeaderItem.forScript(this.render()));
		
	}

}
