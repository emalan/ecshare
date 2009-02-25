package com.madalla.webapp.cms;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.Resource;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.ResourceLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.lang.Bytes;

public class EditableResourceLink extends Panel 
{
	private static final long serialVersionUID = 1L;
	private static Bytes MAX_FILE_SIZE = Bytes.kilobytes(5000);

	private boolean editMode = false;
	private Form resourceForm;
	

	/** label component. */
	private Component label;
	
	/** edit data **/
	private ILinkData data;
	
	public interface ILinkData extends Serializable {
		String getName();
		String getTitle();
		WebResource getResource();
		FileUpload getFileUpload();
		String getResourceType();
		void setName(String name);
		void setTitle(String title);
		void setFileUpload(FileUpload upload);
		void setResourceType(String type);
	}
	
	public enum ResourceType {

	    TYPE_PDF("application/pdf", "pdf"), 
	    TYPE_DOC("application/msword", "doc"), 
	    TYPE_ODT("application/vnd.oasis.opendocument.text", "odt");

	    public final String resourceType;
	    public final String suffix;

	    ResourceType(String type, String suffix) {
	        this.resourceType = type;
	        this.suffix = suffix;
	    }
	}
	
	protected class LabelAjaxBehavior extends AjaxEventBehavior
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Construct.
		 *
		 * @param event
		 */
		public LabelAjaxBehavior(String event)
		{
			super(event);
		}

		protected void onEvent(AjaxRequestTarget target)
		{
			onEdit(target);
		}
	}
	
	/**
	 * @param id Wicket Id
	 * @param data Data Object for Model
	 * 
	 */
	public EditableResourceLink(String id, ILinkData data) 
	{
		super(id);
		super.setOutputMarkupId(true);
		this.data = data;
		if (data == null){
			String message = "The Constructor for AjaxEditableLink requires a value for data.";
			throw new WicketRuntimeException(message);
		}
	}
	
	/**
	 * @see org.apache.wicket.MarkupContainer#setModel(org.apache.wicket.model.IModel)
	 */
	public final Component setModel(IModel model)
	{
		String message = "AjaxEditableLink constructs its own model. Do not set it.";
		throw new WicketRuntimeException(message);
	}

	private Form newFileUploadForm(String id)
	{
		final Form form = new Form(id)
		{
			@Override
			protected void onSubmit() {
				EditableResourceLink.this.onModelChanged();
				EditableResourceLink.this.onSubmit();
			}

			private static final long serialVersionUID = 1L;
			
		};
		form.setOutputMarkupId(true);
		form.setVisible(false);
		form.setMaxSize(MAX_FILE_SIZE);
		return form;
	}	
	
	/**
	 * Create a new form component instance to serve as editor.
	 *
	 * @param parent
	 *            The parent component
	 * @param componentId
	 *            Id that should be used by the component
	 * @param model
	 *            The model
	 * @return The editor
	 */
	protected FormComponent newEditor(MarkupContainer parent, String componentId, IModel model)
	{
		final TextField nameEditor = new TextField(componentId, model)
		{
			private static final long serialVersionUID = 1L;

			protected void onModelChanged()
			{
				super.onModelChanged();
				EditableResourceLink.this.onModelChanged();
			}

			protected void onModelChanging()
			{
				super.onModelChanging();
				EditableResourceLink.this.onModelChanging();
			}
		};
		nameEditor.setOutputMarkupId(true);
		
		return nameEditor;
	}

	protected FormComponent newFileUpload(MarkupContainer parent, String componentId, IModel model)
	{
		final FormComponent editor = new FileUploadField(componentId, model)
		{
			private static final long serialVersionUID = 1L;

			protected void onModelChanged()
			{
				super.onModelChanged();
				EditableResourceLink.this.onModelChanged();
			}

			protected void onModelChanging()
			{
				super.onModelChanging();
				EditableResourceLink.this.onModelChanging();
			}
		};
		editor.setOutputMarkupId(true);
		editor.setRequired(true);
		return editor;
	}

	protected Component newEditLink(MarkupContainer parent, String componentId)
	{
		Link link = new Link(componentId)
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("title", "Edit Link");
				super.onComponentTag(tag);
			}

			protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
			{
				if (StringUtils.isEmpty(data.getName()))
				{
					replaceComponentTagBody(markupStream, openTag, defaultNullLabel());
				}
				else 
				{
					replaceComponentTagBody(markupStream, openTag, data.getName());
				}
			}

			@Override
			public void onClick() {
				// TODO Auto-generated method stub
				
			}

		};
		link.setOutputMarkupId(true);
		link.add(new LabelAjaxBehavior(getLabelAjaxEvent()));
		return link;
	}
	
	protected Component newResourceLink(MarkupContainer parent, final Resource resource, String componentId)
	{
		if (null == resource || resource.getResourceStream() == null){
			return new Label(componentId, getString("label.notconfigured"));
		}
		Link link = new ResourceLink(componentId, resource)
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("title", data.getTitle());
				super.onComponentTag(tag);
			}

			protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
			{
				if (StringUtils.isEmpty(data.getName()))
				{
					replaceComponentTagBody(markupStream, openTag, defaultNullLabel());
				}
				else 
				{
					replaceComponentTagBody(markupStream, openTag, data.getName());
				}
			}
			
		};
		link.setOutputMarkupId(true);
		return link;
	}

	/**
	 * By default this returns "onclick" uses can overwrite this on which event the label behavior
	 * should be triggered
	 *
	 * @return The event name
	 */
	protected String getLabelAjaxEvent()
	{
		return "onclick";
	}


	/**
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	protected void onBeforeRender()
	{
		super.onBeforeRender();
		// lazily add label and form
		if (resourceForm == null)
		{
			initLabelForm(getModel());
		}
		


		label.setEnabled(isEnableAllowed() && isEnabled());
	}

	/**
	 * Invoked when the label is in edit mode, and received a cancel event. Typically, nothing
	 * should be done here.
	 *
	 * @param target
	 *            the ajax request target
	 */
	protected void onCancel(AjaxRequestTarget target)
	{
		label.setVisible(true);
		resourceForm.setVisible(false);
		target.addComponent(EditableResourceLink.this);
	}

	/**
	 * Called when the label is clicked and the component is put in edit mode.
	 *
	 * @param target
	 *            Ajax target
	 */
	protected void onEdit(AjaxRequestTarget target)
	{
		label.setVisible(false);
		resourceForm.setVisible(true);
		target.addComponent(EditableResourceLink.this);
		// put focus on the textfield and stupid explorer hack to move the
		// caret to the end
//		target.appendJavascript("{ var el=wicketGet('" + nameEditor.getMarkupId() + "');" +
//			"   if (el.createTextRange) { " +
//			"     var v = el.value; var r = el.createTextRange(); " +
//			"     r.moveStart('character', v.length); r.select(); } }");
//		target.focusComponent(nameEditor);
	}

	/**
	 * Invoked when the label is in edit mode, received a new input, but that input didn't validate
	 *
	 * @param target
	 *            the ajax request target
	 */
	protected void onError(AjaxRequestTarget target)
	{
//		Serializable errorMessage = nameEditor.getFeedbackMessage().getMessage();
//		if (errorMessage instanceof String)
//		{
//			target.appendJavascript("window.status='" +
//				JavascriptUtils.escapeQuotes((String)errorMessage) + "';");
//		}
//		String editorMarkupId = nameEditor.getMarkupId();
//		target.appendJavascript(editorMarkupId + ".select();");
//		target.appendJavascript(editorMarkupId + ".focus();");
//		target.addComponent(nameEditor);
	}

	protected void onSubmit()
	{
		label.setVisible(true);
		resourceForm.setVisible(false);
	}

	/**
	 * Lazy initialization of the label and editor components and set tempModel to null.
	 *
	 * @param model
	 *            The model for the label and editor
	 */
	private void initLabelForm(IModel model)
	{
		
		resourceForm = newFileUploadForm("resource-form");
		add(resourceForm);
		resourceForm.add(
				newEditor(this, "editor", new PropertyModel(data, "name")));
		resourceForm.add(
				newEditor(this, "title-editor", new PropertyModel(data, "title")));
		resourceForm.add(
				newFileUpload(this, "file-upload", new PropertyModel(data, "fileUpload")));
	    if (editMode)
	    {
	    	label = newEditLink(this, "link");
	    }
	    else 
	    {
	        label = newResourceLink(this, data.getResource() , "link");
	    }
	    add(label);
	}


	/**
	 * Override this to display a different value when the model object is null. Default is
	 * <code>...</code>
	 *
	 * @return The string which should be displayed when the model object is null.
	 */
	protected String defaultNullLabel()
	{
		return "...";
	}

	/**
	 * Dummy override to fix WICKET-1239
	 *
	 * @see org.apache.wicket.Component#onModelChanged()
	 */
	protected void onModelChanged()
	{
		super.onModelChanged();
	}

	/**
	 * Dummy override to fix WICKET-1239
	 *
	 * @see org.apache.wicket.Component#onModelChanging()
	 */
	protected void onModelChanging()
	{
		super.onModelChanging();
	}

	protected void setEditMode(boolean editMode) 
	{
		this.editMode = editMode;
	}

	
}
