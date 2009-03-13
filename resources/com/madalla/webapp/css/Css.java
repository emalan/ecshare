package com.madalla.webapp.css;

import org.apache.wicket.behavior.HeaderContributor;

public class Css {

	public static final HeaderContributor YUI_BASE = HeaderContributor.forCss(Css.class,"base.css");
	public static final HeaderContributor YUI_CORE = HeaderContributor.forCss(Css.class,"reset-fonts-grid.css");
	public static final HeaderContributor CSS_BUTTONS = HeaderContributor.forCss(Css.class,"button.css");
	public static final HeaderContributor CSS_IMAGE = HeaderContributor.forCss(Css.class,"image.css");
	public static final HeaderContributor CSS_FORM = HeaderContributor.forCss(Css.class,"form.css");
	public static final HeaderContributor CSS_ICON = HeaderContributor.forCss(Css.class,"icon.css");
	
}
