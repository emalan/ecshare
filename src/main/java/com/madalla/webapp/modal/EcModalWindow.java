package com.madalla.webapp.modal;

import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public class EcModalWindow extends ModalWindow implements IHeaderContributor{
	private static final long serialVersionUID = 1L;

	private static final ResourceReference CSS = new PackageResourceReference(Dialog.class, "modal.css");

	public EcModalWindow(String id) {
		super(id);

		//add own style
       	setCssClassName("w_obo");

	}

	public void renderHead(IHeaderResponse response) {
		response.renderOnDomReadyJavaScript("Wicket.Window.unloadConfirmation = false;");
		response.renderCSSReference(CSS);
	}

}
