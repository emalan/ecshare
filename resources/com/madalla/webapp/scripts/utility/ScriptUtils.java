package com.madalla.webapp.scripts.utility;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

/**
 * Placeholder for Scriptaculous
 * @author Eugene Malan
 *
 */
public class ScriptUtils {

	public static final CompressedResourceReference FADE_POPUP = new CompressedResourceReference(ScriptUtils.class,"fadePopup.js");
	public static final CompressedResourceReference DRAG_DROP = new CompressedResourceReference(ScriptUtils.class,"dragdropobserver.js");
	public static final CompressedResourceReference FAST_INIT = new CompressedResourceReference(ScriptUtils.class,"banner/fastinit.js");
	public static final CompressedResourceReference CROSSFADE = new CompressedResourceReference(ScriptUtils.class,"banner/crossfade.js");
	public static final CompressedResourceReference BANNER = new CompressedResourceReference(ScriptUtils.class,"banner/banner.js");
	public static final HeaderContributor BANNER_CSS = CSSPackageResource.getHeaderContribution(ScriptUtils.class,"banner/banner.css");
	public static final CompressedResourceReference SLIDESHOW = new CompressedResourceReference(ScriptUtils.class,"slideshow/slideshow.js");
	
	private ScriptUtils(){}
}
