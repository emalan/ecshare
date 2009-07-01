package com.madalla.wicket;

import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.form.Form;

public abstract class IndicatingAjaxSubmitLink extends AjaxSubmitLink implements IAjaxIndicatorControl {
	private static final long serialVersionUID = 1L;
	private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();


	public IndicatingAjaxSubmitLink(String id, Form<?> form) {
		super(id, form);
		add(indicatorAppender);

	}

	public String getAjaxIndicatorMarkupId() {
		return indicatorAppender.getMarkupId();
	}

}
