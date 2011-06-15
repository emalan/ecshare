package com.madalla.webapp.scripts.scriptaculous;

import org.apache.wicket.markup.html.resources.CompressedResourceReference;

/**
 * References to Scriptaculous resources
 *
 * @author Eugene Malan
 *
 */
public class Scriptaculous {

    public static final CompressedResourceReference PROTOTYPE = new CompressedResourceReference(Scriptaculous.class,"prototype.js");
    public static final CompressedResourceReference EFFECTS = new CompressedResourceReference(Scriptaculous.class,"effects.js");
    public static final CompressedResourceReference DRAGDROP = new CompressedResourceReference(Scriptaculous.class,"dragdrop.js");

    public static final CompressedResourceReference CONTROLS = new CompressedResourceReference(
    		Scriptaculous.class, "scriptaculous.js?load=effects,controls");

	private Scriptaculous(){}
}
