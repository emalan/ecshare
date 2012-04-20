package com.madalla.webapp;

import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class WebappResources {

	/** TODO use this as default resource instead of webapp resource */
	public static final ResourceReference EDITICON = new PackageResourceReference(
			WebappResources.class, "pencil_icon.gif");


}
