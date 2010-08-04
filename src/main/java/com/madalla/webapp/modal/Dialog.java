package com.madalla.webapp.modal;

import org.apache.wicket.Component;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.string.AppendingStringBuffer;

public class Dialog extends ModalWindow implements IHeaderContributor{
	private static final long serialVersionUID = 1L;
	
	//private final Log log = LogFactory.getLog(this.getClass());
	private final boolean saveButton;
	private final boolean closeButton = true;
	private Component component;
	private String dataId; 
	private String cancelButtonLabel = "Done";

	private static final ResourceReference OBO = new CompressedResourceReference(Dialog.class, "modal.css");
	
	public Dialog(final String id){
		this(id, false);
	}
	public Dialog(final String id, boolean saveButton) {
		super(id);
		this.saveButton = saveButton;
      	add(CSSPackageResource.getHeaderContribution(OBO));
       	setCssClassName("w_obo");
       	
        setInitialWidth(450);
        setInitialHeight(300);
       	setResizable(false);
	}
	

	@Override
	public ModalWindow setContent(final Component component) {
		this.component = component;
		
		if (component.getId().equals("dialogContent") == false)
		{
			throw new WicketRuntimeException("Dialog content id is wrong. Component ID:" +
				component.getId() + " : should be dialogContent. ");
		}
		
		Component panel = new DialogContentPanel(this.getContentId(), component, closeButton, saveButton){
			private static final long serialVersionUID = 1L;

			void onCancel(AjaxRequestTarget target) {
            	close(target);
            }

			@Override
			void onSave(AjaxRequestTarget target) {
				close(target);
			}

			@Override
			String getCancelButtonString() {
				return cancelButtonLabel;
			}

        };
		
		return super.setContent(panel);
	}
	
	public void setDataId(String id){
		this.dataId = id;
	}

	public String getDataId(){
		return dataId;
	}


	public void renderHead(IHeaderResponse response) {
		response.renderOnDomReadyJavascript("Wicket.Window.unloadConfirmation = false;");
		
		
	}
	
	@Override
	protected AppendingStringBuffer postProcessSettings(AppendingStringBuffer settings) {
		return super.postProcessSettings(settings);
	}


	@Override
	protected void onBeforeRender() {
		if (component != null){
			IModel<String> height = new Model<String>("height: " + (getInitialHeight()-50) + "px; width: " + (getInitialWidth()-20) + "px");
			component.add(new AttributeAppender("style", true, height, "; "));
		}
		super.onBeforeRender();
	}
	
	public void setCancelButtonLabel(String label){
		this.cancelButtonLabel = label;
	}
	
}