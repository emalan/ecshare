package com.madalla.webapp.cms;

import java.io.Serializable;

import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.ajax.AbstractDefaultAjaxBehavior;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.DynamicWebResource;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.string.JavascriptUtils;

public class AjaxEditableLink extends Panel 
{
	private static final long serialVersionUID = 1L;

	/** editor component. */
	private FormComponent editor;

	/** label component. */
	private Component label;
	
	/** editable data **/
	private ILinkData data;
	
	public interface ILinkData extends Serializable {
		String getName();
		String getTitle();
		DynamicWebResource getResource();
		void setName(String name);
		void setTitle(String title);
	}
	
	protected class EditorAjaxBehavior extends AbstractDefaultAjaxBehavior
	{
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor.
		 */
		public EditorAjaxBehavior()
		{
		}

		protected void onComponentTag(ComponentTag tag)
		{
			super.onComponentTag(tag);
			final String saveCall = "{" +
				generateCallbackScript("wicketAjaxGet('" + getCallbackUrl() +
					"&save=true&'+this.name+'='+wicketEncode(this.value)") + "; return false;}";


			final String cancelCall = "{" +
				generateCallbackScript("wicketAjaxGet('" + getCallbackUrl() + "&save=false'") +
				"; return false;}";


			final String keypress = "var kc=wicketKeyCode(event); if (kc==27) " + cancelCall +
				" else if (kc!=13) { return true; } else " + saveCall;

			tag.put("onblur", saveCall);
			tag.put("onkeypress", keypress);

		}

		protected void respond(AjaxRequestTarget target)
		{
			RequestCycle requestCycle = RequestCycle.get();
			boolean save = Boolean.valueOf(requestCycle.getRequest().getParameter("save"))
				.booleanValue();

			if (save)
			{
				editor.processInput();

				if (editor.isValid())
				{
					onSubmit(target);
				}
				else
				{
					onError(target);
				}
			}
			else
			{
				onCancel(target);
			}
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
	
	public AjaxEditableLink(String id, ILinkData data) 
	{
		super(id);
		super.setOutputMarkupId(true);
		this.data = data;
	}
	
	/**
	 * @see org.apache.wicket.MarkupContainer#setModel(org.apache.wicket.model.IModel)
	 */
	public final Component setModel(IModel model)
	{
		//TODO throw Exception
		return this;
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
	protected FormComponent newEditor(MarkupContainer parent, String componentId)
	{
		TextField nameEditor = new TextField(componentId, new PropertyModel(data, "name"))
		{
			private static final long serialVersionUID = 1L;

			protected void onModelChanged()
			{
				super.onModelChanged();
				AjaxEditableLink.this.onModelChanged();
			}

			protected void onModelChanging()
			{
				super.onModelChanging();
				AjaxEditableLink.this.onModelChanging();
			}
		};
		nameEditor.setOutputMarkupId(true);
		nameEditor.setVisible(false);
		nameEditor.add(new EditorAjaxBehavior());
		return nameEditor;
	}

	/**
	 * Create a new form component instance to serve as label.
	 *
	 * @param parent
	 *            The parent component
	 * @param componentId
	 *            Id that should be used by the component
	 * @param model
	 *            The model
	 * @return The editor
	 */
	protected Component newLink(MarkupContainer parent, String componentId)
	{
		Link link = new Link(componentId)
		{
			private static final long serialVersionUID = 1L;
			
			@Override
			protected void onComponentTag(ComponentTag tag) {
				tag.put("title", data.getTitle());
				super.onComponentTag(tag);
			}

			protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
			{
				replaceComponentTagBody(markupStream, openTag, data.getName());
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
		// lazily add label and editor
		if (editor == null)
		{
			initLabelAndEditor(getModel());
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
		editor.setVisible(false);
		target.addComponent(AjaxEditableLink.this);
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
		editor.setVisible(true);
		target.addComponent(AjaxEditableLink.this);
		// put focus on the textfield and stupid explorer hack to move the
		// caret to the end
		target.appendJavascript("{ var el=wicketGet('" + editor.getMarkupId() + "');" +
			"   if (el.createTextRange) { " +
			"     var v = el.value; var r = el.createTextRange(); " +
			"     r.moveStart('character', v.length); r.select(); } }");
		target.focusComponent(editor);
	}

	/**
	 * Invoked when the label is in edit mode, received a new input, but that input didn't validate
	 *
	 * @param target
	 *            the ajax request target
	 */
	protected void onError(AjaxRequestTarget target)
	{
		Serializable errorMessage = editor.getFeedbackMessage().getMessage();
		if (errorMessage instanceof String)
		{
			target.appendJavascript("window.status='" +
				JavascriptUtils.escapeQuotes((String)errorMessage) + "';");
		}
		String editorMarkupId = editor.getMarkupId();
		target.appendJavascript(editorMarkupId + ".select();");
		target.appendJavascript(editorMarkupId + ".focus();");
		target.addComponent(editor);
	}

	/**
	 * Invoked when the editor was successfully updated. Use this method e.g. to persist the changed
	 * value. This implementation displays the label and clears any window status that might have
	 * been set in onError.
	 *
	 * @param target
	 *            The ajax request target
	 */
	protected void onSubmit(AjaxRequestTarget target)
	{
		label.setVisible(true);
		editor.setVisible(false);
		target.addComponent(AjaxEditableLink.this);

		target.appendJavascript("window.status='';");
	}

	/**
	 * Lazy initialization of the label and editor components and set tempModel to null.
	 *
	 * @param model
	 *            The model for the label and editor
	 */
	private void initLabelAndEditor(IModel model)
	{
		editor = newEditor(this, "editor");
		label = newLink(this, "link");
		add(label);
		add(editor);
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

	
}
