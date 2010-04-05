package com.madalla.wicket.resourcelink;

import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.IModel;

import com.madalla.wicket.resourcelink.EditableResourceLink.ILinkData;

public class IndicatingUploadLink extends ExternalLink {

	private static final long serialVersionUID = 1L;

	private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();

	public IndicatingUploadLink(String id, final ILinkData data, IModel<String> href, IModel<String> label) {
		super(id, href, label);
		add(indicatorAppender);
	}
	
	public String getAjaxIndicatorMarkupId() {
		return indicatorAppender.getMarkupId();
	}

	
}
