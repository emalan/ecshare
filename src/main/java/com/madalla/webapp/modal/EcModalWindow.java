package com.madalla.webapp.modal;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;

public class EcModalWindow extends ModalWindow implements IHeaderContributor{
	private static final long serialVersionUID = 1L;

	private static final ResourceReference CSS = new CompressedResourceReference(Dialog.class, "modal.css");

	public EcModalWindow(String id) {
		super(id);

		//add own style
		add(CSSPackageResource.getHeaderContribution(CSS));
       	setCssClassName("w_obo");

	}

	public void renderHead(IHeaderResponse response) {
		response.renderOnDomReadyJavascript("Wicket.Window.unloadConfirmation = false;");
	}

}
