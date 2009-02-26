package com.madalla.webapp.scripts;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;

/**
 * Javascript Header resources
 * @author Eugene Malan
 *
 */
public class JavascriptResources {

	public static final HeaderContributor PROTOTYPE = HeaderContributor.forJavaScript(
			new CompressedResourceReference(Scriptaculous.class,"prototype.js"));
	
	public static final HeaderContributor SCRIPTACULOUS_CONTROLS = HeaderContributor.forJavaScript(
			Scriptaculous.CONTROLS);
	
	public static final HeaderContributor SCRIPT_UTILS = HeaderContributor.forJavaScript(
			new CompressedResourceReference(JavascriptResources.class, "common.js"));

}
