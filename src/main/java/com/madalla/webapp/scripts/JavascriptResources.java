package com.madalla.webapp.scripts;

import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;

/**
 * Javascript Resources
 *
 * @author Eugene Malan
 *
 */
public class JavascriptResources {

	public static final ResourceReference ANIMATOR = new PackageResourceReference(JavascriptResources.class, "animator.js");

	public static final ResourceReference PROTOTYPE = Scriptaculous.PROTOTYPE;

	public static final ResourceReference SCRIPT_UTILS = new PackageResourceReference(JavascriptResources.class, "common.js");

	public static final ResourceReference SWF_OBJECT =	new PackageResourceReference(JavascriptResources.class, "swfobject.js");


}
