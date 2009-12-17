package com.madalla.wicket.configure;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import com.madalla.webapp.css.Css;
import com.madalla.wicket.animation.AbstractAnimationEventBehavior;
import com.madalla.wicket.animation.base.Animator;
import com.madalla.wicket.animation.base.DiscreteSubject;
import com.madalla.wicket.animation.base.NumericSubject;

public class AjaxConfigureIcon extends WebMarkupContainer{
	private static final long serialVersionUID = 1L;
	
	public static final HeaderContributor SCRIPT_UTILS = JavascriptPackageResource.getHeaderContribution(
			new CompressedResourceReference(AjaxConfigureIcon.class, "resourcelink.js"));
	
	public AjaxConfigureIcon(String id, final Component configureArea, final int size){
		this(id, null, configureArea, size);
	}

	public AjaxConfigureIcon(String id, final Component displayArea, final Component configureArea, final int size) {
		super(id);
		if (displayArea != null) {
			displayArea.setOutputMarkupId(true);
		}
		
		add(Css.CSS_ICON);
		add(SCRIPT_UTILS);
		
		add(new AbstractAnimationEventBehavior("onclick", 1000){
			private static final long serialVersionUID = 1L;

			@Override
			protected void addAnimatorSubjects(Animator animator) {
				if (displayArea != null) {
					animator.addSubject(new DiscreteSubject(displayArea.getMarkupId(),"display", "", "none", 0.05));
				}
				animator.addSubject(new DiscreteSubject(configureArea.getMarkupId(), "display", "none","", 0.1))
					.addSubject(new NumericSubject(configureArea.getMarkupId(),"opacity", 0.0, 1.0))
					.addSubject(new NumericSubject(configureArea.getMarkupId(), "height", 1, size,"em"));				
			}

			@Override
			protected String onEventAnimatorActions(Animator animator) {
				return animator.toggle();
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return new IAjaxCallDecorator() {

					private static final long serialVersionUID = 1L;

					public CharSequence decorateOnFailureScript(CharSequence script) {
						return script;
					}

					public CharSequence decorateOnSuccessScript(CharSequence script) {
						return script;
					}

					public CharSequence decorateScript(CharSequence script) {
						String post = "var e = Wicket.$('" + configureArea.getParent().getMarkupId()
								+ "'); if (Utils.hasClassName(e, 'editing'))"
								+ "{Utils.removeClassName(e, 'editing');} else "
								+ "{Utils.addClassName(e, 'editing')};";
						return script + post;
					}

				};
			}

			
		});
		setOutputMarkupId(true);
	}
	
	@Override
	protected void onComponentTag(ComponentTag tag) {
		tag.put("title", getString("label.configure"));
		super.onComponentTag(tag);
	}
}

