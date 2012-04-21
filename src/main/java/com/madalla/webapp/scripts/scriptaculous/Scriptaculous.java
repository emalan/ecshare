package com.madalla.webapp.scripts.scriptaculous;

import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;


/**
 * References to Scriptaculous resources
 *
 * @author Eugene Malan
 *
 */
public class Scriptaculous {

    public static final ResourceReference PROTOTYPE = new PackageResourceReference(Scriptaculous.class,"prototype.js");
    public static final ResourceReference EFFECTS = new PackageResourceReference(Scriptaculous.class,"effects.js");
    public static final ResourceReference DRAGDROP = new PackageResourceReference(Scriptaculous.class,"dragdrop.js");

    public static final ResourceReference CONTROLS = new PackageResourceReference(
    		Scriptaculous.class, "scriptaculous.js?load=effects,controls");

	private Scriptaculous(){}
}
