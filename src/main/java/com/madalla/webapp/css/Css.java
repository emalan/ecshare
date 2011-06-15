package com.madalla.webapp.css;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.CSSPackageResource;

public class Css {

	public static final HeaderContributor BASE = CSSPackageResource.getHeaderContribution(Css.class,"base.css");
	public static final HeaderContributor YUI_CORE = CSSPackageResource.getHeaderContribution(Css.class,"reset-fonts-grid.css");
	public static final HeaderContributor CSS_BUTTONS = CSSPackageResource.getHeaderContribution(Css.class,"button.css");
	public static final HeaderContributor CSS_IMAGE = CSSPackageResource.getHeaderContribution(Css.class,"image.css");
	public static final HeaderContributor CSS_FORM = CSSPackageResource.getHeaderContribution(Css.class,"form.css");
	public static final HeaderContributor CSS_ICON = CSSPackageResource.getHeaderContribution(Css.class,"icon.css");

}
