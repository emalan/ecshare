package com.madalla.wicket.animation;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.IHeaderResponse;

import com.madalla.webapp.scripts.JavascriptResources;

public abstract class AbstractAnimationBehavior extends AjaxEventBehavior {
	private static final long serialVersionUID = 1L;

	private final Animator animator;
	
	public AbstractAnimationBehavior(String event) {
		super(event);
		animator = new Animator();
	}
	
	@Override
	public void beforeRender(Component component){
		
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		animator.setMarkupId(getComponent().getMarkupId());
		addAnimatorSubjects(animator);
		response.renderJavascriptReference(JavascriptResources.ANIMATOR);
		response.renderOnDomReadyJavascript(animator.render());
	}
	
	@Override
	protected void onEvent(AjaxRequestTarget target) {
		target.appendJavascript(onEventAnimatorActions(animator));	
	}
	
	abstract protected String onEventAnimatorActions(final Animator animator);
	abstract protected void addAnimatorSubjects(final Animator animator) ;
	
}
