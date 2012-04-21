package com.madalla.wicket.configure;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import com.madalla.webapp.css.Css;
import com.madalla.wicket.animation.AnimationEventBehavior;
import com.madalla.wicket.animation.AnimatorSubject;
import com.madalla.wicket.animation.IAnimator;
import com.madalla.wicket.animation.IAnimatorActions;

public class AjaxConfigureIcon extends WebMarkupContainer{
	private static final long serialVersionUID = 1L;

	public static final ResourceReference SCRIPT_UTILS = new PackageResourceReference(AjaxConfigureIcon.class,"resourcelink.js");

	public AjaxConfigureIcon(String id, final Component configureArea, final int size){
		this(id, null, configureArea, size);
	}

	public AjaxConfigureIcon(String id, final Component displayArea, final Component configureArea, final int size) {
		super(id);
		if (displayArea != null) {
			displayArea.setOutputMarkupId(true);
		}


		add(new AnimationEventBehavior("onclick", 1000){
			private static final long serialVersionUID = 1L;

			@Override
			protected void addAnimatorSubjects(IAnimator animator) {
				if (displayArea != null) {
					animator.addSubject(AnimatorSubject.discrete(displayArea.getMarkupId(),"display", "", "none", 0.05));
				}
				animator.addSubjects(AnimatorSubject.slideOpen(configureArea.getMarkupId(),size));

			}

			@Override
			protected String onEventAnimatorActions(final IAnimatorActions animator) {
				return animator.toggle();
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return new IAjaxCallDecorator() {

					private static final long serialVersionUID = 1L;

					@Override
					public CharSequence decorateScript(Component component,
							CharSequence script) {
						String post = "var e = Wicket.$('" + configureArea.getParent().getMarkupId()
								+ "'); if (Utils.hasClassName(e, 'editing'))"
								+ "{Utils.removeClassName(e, 'editing');} else "
								+ "{Utils.addClassName(e, 'editing')};";
						return script + post;
					}

					@Override
					public CharSequence decorateOnSuccessScript(
							Component component, CharSequence script) {
						return script;
					}

					@Override
					public CharSequence decorateOnFailureScript(
							Component component, CharSequence script) {
						return script;
					}

				};
			}

		});

		setOutputMarkupId(true);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference(Css.CSS_ICON);
		response.renderCSSReference(SCRIPT_UTILS);

	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		tag.put("title", getString("label.configure"));
		super.onComponentTag(tag);
	}
}

