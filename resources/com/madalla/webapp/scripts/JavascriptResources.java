package com.madalla.webapp.scripts;

import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

import com.madalla.webapp.scripts.scriptaculous.Scriptaculous;

/**
 * Javascript Header resources
 * @author Eugene Malan
 *
 */
public class JavascriptResources {

	public static final HeaderContributor PROTOTYPE = JavascriptPackageResource.getHeaderContribution(
			new CompressedResourceReference(Scriptaculous.class,"prototype.js"));
	
	public static final HeaderContributor SCRIPTACULOUS_CONTROLS = JavascriptPackageResource.getHeaderContribution(
			Scriptaculous.CONTROLS);
	
	public static final HeaderContributor SCRIPT_UTILS = JavascriptPackageResource.getHeaderContribution(
			new CompressedResourceReference(JavascriptResources.class, "common.js"));

	public static final HeaderContributor SWF_OBJECT = JavascriptPackageResource.getHeaderContribution(
			new CompressedResourceReference(JavascriptResources.class, "swfobject.js"));
	
	public static final HeaderContributor ANIMATOR = JavascriptPackageResource.getHeaderContribution(
	        new CompressedResourceReference(JavascriptResources.class, "animator.js"));

}
