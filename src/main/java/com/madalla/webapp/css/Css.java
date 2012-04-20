package com.madalla.webapp.css;

import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class Css {

	public static final ResourceReference BASE = new PackageResourceReference(Css.class, "base.css");
	public static final ResourceReference YUI_CORE = new PackageResourceReference(Css.class,"reset-fonts-grid.css");
	public static final ResourceReference CSS_BUTTONS = new PackageResourceReference(Css.class,"button.css");
	public static final ResourceReference CSS_IMAGE = new PackageResourceReference(Css.class,"image.css");
	public static final ResourceReference CSS_FORM = new PackageResourceReference(Css.class,"form.css");
	public static final ResourceReference CSS_ICON = new PackageResourceReference(Css.class,"icon.css");

}
