package com.madalla.webapp.scripts.utility;

import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;


/**
 * Placeholder for Scriptaculous
 * @author Eugene Malan
 *
 */
public class ScriptUtils {

	public static final ResourceReference FADE_POPUP = new PackageResourceReference(ScriptUtils.class,"fadePopup.js");
	public static final ResourceReference DRAG_DROP = new PackageResourceReference(ScriptUtils.class,"dragdropobserver.js");
	public static final ResourceReference FAST_INIT = new PackageResourceReference(ScriptUtils.class,"banner/fastinit.js");
	public static final ResourceReference CROSSFADE = new PackageResourceReference(ScriptUtils.class,"banner/crossfade.js");
	public static final ResourceReference CROSSFADE_CSS = new PackageResourceReference(ScriptUtils.class,"banner/crossfade.css");
	public static final ResourceReference BANNER = new PackageResourceReference(ScriptUtils.class,"banner/banner.js");
	public static final ResourceReference BANNER_CSS = new PackageResourceReference(ScriptUtils.class,"banner/banner.css");
	public static final ResourceReference SLIDESHOW = new PackageResourceReference(ScriptUtils.class,"slideshow/slideshow.js");
	public static final ResourceReference SLIDESHOW_CSS = new PackageResourceReference(ScriptUtils.class,"slideshow/slideshow.css");

	private ScriptUtils(){}
}
