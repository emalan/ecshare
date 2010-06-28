package com.madalla.webapp.user;

import java.text.MessageFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

import com.madalla.webapp.CmsApplication;
import com.madalla.webapp.panel.CmsPanel;

public class RpxPanel extends CmsPanel{
	private static final long serialVersionUID = 1L;

	public RpxPanel(String id) {
		super(id);
		add(new Label("rpxHeading", new Model<String>(getString("label.rpx"))));
		add(new WebComponent("openidWidget"){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onComponentTag(ComponentTag tag) {
				super.onComponentTag(tag);
				final String siteUrl = getRepositoryService().getSiteData().getUrl();
				final String lang = getSession().getLocale().getLanguage();
				final String hideHeading = "flags=hide_sign_in_with";
				if (StringUtils.isNotEmpty(siteUrl)){
					String callback = ((CmsApplication)getApplication()).getRpxService().getCallback();
					tag.put("src", MessageFormat.format(callback, new Object[]{siteUrl, lang}) + "&" + hideHeading);
				}
			}
			
			
		});
	}

}
