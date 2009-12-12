package com.madalla.webapp.scripts;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;

/**
 * Javascript Resources
 * 
 * @author Eugene Malan
 *
 */
public class JavascriptResources {

	public static final ResourceReference ANIMATOR = new CompressedResourceReference(JavascriptResources.class, "animator.js");

	public static final ResourceReference PROTOTYPE = Scriptaculous.PROTOTYPE;
	
	public static final ResourceReference SCRIPT_UTILS = new CompressedResourceReference(JavascriptResources.class, "common.js");

	public static final ResourceReference SWF_OBJECT =	new CompressedResourceReference(JavascriptResources.class, "swfobject.js");
	

}
