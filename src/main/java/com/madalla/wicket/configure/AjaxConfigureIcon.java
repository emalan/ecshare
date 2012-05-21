package com.madalla.wicket.configure;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IComponentAwareHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;

import com.madalla.webapp.css.Css;
import com.madalla.webapp.scripts.JavascriptResources;
import com.madalla.wicket.animation.AnimationEventBehavior;
import com.madalla.wicket.animation.AnimatorSubject;
import com.madalla.wicket.animation.IAnimator;
import com.madalla.wicket.animation.IAnimatorActions;

public class AjaxConfigureIcon extends WebMarkupContainer{
	private static final long serialVersionUID = 1L;

	private class AjaxConfigureCallDecorator implements IAjaxCallDecorator, IComponentAwareHeaderContributor {
		private static final long serialVersionUID = 1L;
		private final Component configureArea;
		
		public AjaxConfigureCallDecorator(final Component configureArea) {
			this.configureArea = configureArea;
		}
		
		public CharSequence decorateScript(Component component, CharSequence script) {
			String post = "var e = Wicket.$('" + configureArea.getParent().getMarkupId()
					+ "'); if (Utils.hasClassName(e, 'editing'))"
					+ "{Utils.removeClassName(e, 'editing');} else "
					+ "{Utils.addClassName(e, 'editing')};";
			return script + post;
		}

		public CharSequence decorateOnSuccessScript(Component component, CharSequence script) {
			return script;
		}

		public CharSequence decorateOnFailureScript(Component component, CharSequence script) {
			return script;
		}

		public void renderHead(Component component, IHeaderResponse response) {
			response.renderJavaScriptReference(JavascriptResources.SCRIPT_UTILS);
		}
		
	}

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
				return new AjaxConfigureCallDecorator(configureArea);
			}

		});

		setOutputMarkupId(true);
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		response.renderCSSReference(Css.CSS_ICON);
	}

	@Override
	protected void onComponentTag(ComponentTag tag) {
		tag.put("title", getString("label.configure"));
		super.onComponentTag(tag);
	}
}

