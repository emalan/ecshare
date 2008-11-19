package com.madalla.webapp.scripts.utility;

import org.apache.wicket.markup.html.resources.CompressedResourceReference;

public class ScriptUtils {

	public static final CompressedResourceReference FADE_POPUP = new CompressedResourceReference(ScriptUtils.class,"fadePopup.js");
	public static final CompressedResourceReference DRAG_DROP = new CompressedResourceReference(ScriptUtils.class,"dragdropobserver.js");
	public static final CompressedResourceReference FAST_INIT = new CompressedResourceReference(ScriptUtils.class,"banner/fastinit.js");
	public static final CompressedResourceReference CROSSFADE = new CompressedResourceReference(ScriptUtils.class,"banner/crossfade.js");
	public static final CompressedResourceReference BANNER = new CompressedResourceReference(ScriptUtils.class,"banner/banner.js");
	
	private ScriptUtils(){}
}
