package com.madalla.wicket.configure;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import com.madalla.webapp.css.Css;
import com.madalla.wicket.animation.AbstractAnimationEventBehavior;
import com.madalla.wicket.animation.base.Animator;
import com.madalla.wicket.animation.base.DiscreteSubject;
import com.madalla.wicket.animation.base.NumericSubject;

public class AjaxConfigureIcon extends Panel{
	private static final long serialVersionUID = 1L;
	
	public static final HeaderContributor SCRIPT_UTILS = JavascriptPackageResource.getHeaderContribution(
			new CompressedResourceReference(AjaxConfigureIcon.class, "resourcelink.js"));
	

	public AjaxConfigureIcon(String id, final Component displayArea, final Component configureArea, final Component upload) {
		super(id);
		displayArea.setOutputMarkupId(true);
		add(Css.CSS_ICON);
		add(SCRIPT_UTILS);
		final MarkupContainer configureContainer = new WebMarkupContainer("editContainer");
		
		Component link = new Label("configure","Configure"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("title", getString("label.configure"));
				super.onComponentTag(tag);
			}
			
		};
		link.add(new AbstractAnimationEventBehavior("onclick"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void addAnimatorSubjects(Animator animator) {
				animator.addSubject(new DiscreteSubject(displayArea.getMarkupId(),"display", "", "none", 0.1));
				animator.addSubject(new DiscreteSubject(configureArea.getMarkupId(), "display", "none","", 0.2));
				//TODO height should be passed in
				animator.addSubject(new NumericSubject(configureArea.getMarkupId(), "height", 1, 17,"em"));				
			}

			@Override
			protected String onEventAnimatorActions(Animator animator) {
				return animator.toggle();
			}

			@Override
			protected void onEvent(AjaxRequestTarget target) {
				super.onEvent(target);
				target.focusComponent(upload);
			}

			
		});

//		link.add(new AnimationOpenSlide(configureArea, 14, "em") {
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected IAjaxCallDecorator getAjaxCallDecorator() {
//				return new IAjaxCallDecorator() {
//
//					private static final long serialVersionUID = 1L;
//
//					public CharSequence decorateOnFailureScript(CharSequence script) {
//						return script;
//					}
//
//					public CharSequence decorateOnSuccessScript(CharSequence script) {
//						return script;
//					}
//
//					public CharSequence decorateScript(CharSequence script) {
//						String post = "var e = Wicket.$('" + container.getMarkupId()
//								+ "'); if (Utils.hasClassName(e, 'editing'))"
//								+ "{Utils.removeClassName(e, 'editing');} else "
//								+ "{Utils.addClassName(e, 'editing')};";
//						return script + post;
//					}
//
//				};
//			}
//
//		});
		
		
		configureContainer.add(link);
		configureContainer.setOutputMarkupId(true);
		add(configureContainer);
		
//		Component link = new AjaxLink("configure"){
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onComponentTag(ComponentTag tag) {
//				tag.put("title", getString("label.configure"));
//				super.onComponentTag(tag);
//			}
//
//			@Override
//			protected IAjaxCallDecorator getAjaxCallDecorator() {
//				return new IAjaxCallDecorator(){
//
//					private static final long serialVersionUID = 1L;
//
//					public CharSequence decorateOnFailureScript(CharSequence script) {
//						return script;
//					}
//
//					public CharSequence decorateOnSuccessScript(CharSequence script) {
//						return script;
//					}
//
//					public CharSequence decorateScript(CharSequence script) {
//						String s =("var e = Wicket.$('"+container.getMarkupId()+"'); if (Utils.hasClassName(e, 'editing'))"+
//								"{Utils.removeClassName(e, 'editing');wicketHide('" + configureAreaId + "');} else "+
//								"{Utils.addClassName(e, 'editing');wicketShow('" + configureAreaId + "');};");
//						return script + s;
//					}
//					
//				};
//			}
//
//			@Override
//			public void onClick(AjaxRequestTarget target) {
//
//			}
//
//		};

	}
	
//	private void hideConfigureArea(AjaxRequestTarget target){
//		hide(target, configureArea.getMarkupId());
//	}
//	
//	private static void hide(AjaxRequestTarget target, String hideAreaId){
//		String s =("var e = Wicket.$('edit-wrapper'); if (Utils.hasClassName(e, 'editing'))"+
//				"{Utils.removeClassName(e, 'editing');wicketHide('" + hideAreaId + "');} else "+
//				"{Utils.addClassName(e, 'editing');wicketShow('" + hideAreaId + "');};");
//		target.appendJavascript(s);
//	}



}
