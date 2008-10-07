package com.madalla.webapp.scripts.utility;

import org.apache.wicket.markup.html.resources.CompressedResourceReference;

public class ScriptUtils {

	public static final CompressedResourceReference FADE_POPUP = new CompressedResourceReference(ScriptUtils.class,"fadePopup.js");
	public static final CompressedResourceReference DRAG_DROP = new CompressedResourceReference(ScriptUtils.class,"dragdrop.js");
	
	private ScriptUtils(){};
}
