package com.madalla.wicket.configure;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import com.madalla.webapp.css.Css;

public class AjaxConfigureIcon extends Panel{

	private static final long serialVersionUID = 1L;
	public static final HeaderContributor SCRIPT_UTILS = JavascriptPackageResource.getHeaderContribution(
			new CompressedResourceReference(AjaxConfigureIcon.class, "resourcelink.js"));

	private String configureAreaId;

	public AjaxConfigureIcon(String id, final String configureAreaId) {
		super(id);
		this.configureAreaId = configureAreaId;
		add(Css.CSS_ICON);
		add(SCRIPT_UTILS);
		Component link = new AjaxLink("configure"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("title", getString("label.configure", null, "Click to Configure"));
				super.onComponentTag(tag);
			}

			@Override
			protected IAjaxCallDecorator getAjaxCallDecorator() {
				return new IAjaxCallDecorator(){

					private static final long serialVersionUID = 1L;

					public CharSequence decorateOnFailureScript(CharSequence script) {
						return script;
					}

					public CharSequence decorateOnSuccessScript(CharSequence script) {
						return script;
					}

					public CharSequence decorateScript(CharSequence script) {
						String s =("var e = Wicket.$('edit-wrapper'); if (Utils.hasClassName(e, 'editing'))"+
								"{Utils.removeClassName(e, 'editing');wicketHide('" + configureAreaId + "');} else "+
								"{Utils.addClassName(e, 'editing');wicketShow('" + configureAreaId + "');};");
						return script + s;
					}
					
				};
			}

			@Override
			public void onClick(AjaxRequestTarget target) {

			}

		};
		add(link);
	}
	
	public void hideConfigureArea(AjaxRequestTarget target){
		hide(target, configureAreaId);
	}
	
	public static void hide(AjaxRequestTarget target, String hideAreaId){
		String s =("var e = Wicket.$('edit-wrapper'); if (Utils.hasClassName(e, 'editing'))"+
				"{Utils.removeClassName(e, 'editing');wicketHide('" + hideAreaId + "');} else "+
				"{Utils.addClassName(e, 'editing');wicketShow('" + hideAreaId + "');};");
		target.appendJavascript(s);
	}



}
