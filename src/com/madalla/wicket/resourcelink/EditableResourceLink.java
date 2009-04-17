package com.madalla.wicket.resourcelink;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupStream;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.JavascriptPackageResource;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.SubmitLink;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.util.time.Duration;

import com.madalla.webapp.cms.IFileUploadStatus;
import com.madalla.webapp.css.Css;
import com.madalla.wicket.AjaxSelfUpdatingLabel;
import com.madalla.wicket.configure.AjaxConfigureIcon;

public class EditableResourceLink extends Panel {
	private static final long serialVersionUID = 1L;
	private static Bytes MAX_FILE_SIZE = Bytes.kilobytes(5000);
	private static final Log log = LogFactory.getLog(EditableResourceLink.class);
	public static final HeaderContributor SCRIPT_UTILS = JavascriptPackageResource.getHeaderContribution(new CompressedResourceReference(EditableResourceLink.class, "resourcelink.js")); 
	
	private Form resourceForm;
	private boolean active;

	/** edit data **/
	private ILinkData data;

	public interface ILinkData extends Serializable {
		String getId();

		String getName();

		String getTitle();
		
		ResourceReference getResourceReference();
		
		FileUpload getFileUpload();

		String getResourceType();

		Boolean getHideLink();

		void setName(String name);

		void setTitle(String title);

		void setFileUpload(FileUpload upload);
		
		void setResourceType(String type);

		void setHideLink(Boolean hide);

        String getUrl();
        
        void setUrl(String url);

	}

	public enum ResourceType {

		TYPE_PDF("application/pdf", "pdf", "Adobe PDF"), 
		TYPE_DOC("application/msword", "doc", "Word document"), 
		TYPE_ODT("application/vnd.oasis.opendocument.text", "odt", "ODT document");

		public final String resourceType; // Mime Type
		public final String suffix; // File suffix
		public final String description;

		ResourceType(String type, String suffix, String description) {
			this.resourceType = type;
			this.suffix = suffix;
			this.description = description;
		}

		/**
		 * @param s
		 *            the separator
		 * @return Separated list of suffixes e.g. doc,pdf,htm
		 */
		public static String getResourceTypeSuffixes(String s) {
			StringBuffer sb = new StringBuffer();
			for (ResourceType type : ResourceType.values()) {
				sb.append(s + type.suffix);
			}
			return sb.toString().substring(1);
		}

	}

	/**
	 * Adds Behavior to the File Upload Form
	 * 
	 * @author Eugene Malan
	 * 
	 */
	protected class FileUploadBehavior extends AjaxEventBehavior {

		private static final long serialVersionUID = 1L;

		final Component name;
		final Component choice;

		public FileUploadBehavior(String event, final Component name, final Component choice) {
			super(event);
			this.name = name;
			this.choice = choice;
		}

		/*
		 * (non-Javadoc) Script that translates the suffix of a selected file to
		 * the Type value that is persisted in the CMS and used by the Type Drop
		 * Down.
		 * 
		 * @see
		 * org.apache.wicket.ajax.AbstractDefaultAjaxBehavior#renderHead(org
		 * .apache.wicket.markup.html.IHeaderResponse)
		 */
		@Override
		public void renderHead(IHeaderResponse response) {
			StringBuffer sb = new StringBuffer();
			sb.append("Utils.translateSuffix = function(suffix){");
			for (ResourceType type : ResourceType.values()) {
				sb.append("if (suffix.toLowerCase() == '" + type.suffix + "') return '" + type + "';");
			}
			sb.append("return '';");
			sb.append("};");

			response.renderJavascript(sb.toString(), "translateSuffix");
			super.renderHead(response);
		}

		@Override
		protected IAjaxCallDecorator getAjaxCallDecorator() {
			return new IAjaxCallDecorator() {

				private static final long serialVersionUID = 1L;

				public CharSequence decorateOnFailureScript(CharSequence script) {
					return script;
				}

				public CharSequence decorateOnSuccessScript(CharSequence script) {
					return script;
				}

				public CharSequence decorateScript(CharSequence script) {
					StringBuffer sb = new StringBuffer("var v = Wicket.$(" + getComponent().getMarkupId() + ").value;");
					String suffixes = ResourceType.getResourceTypeSuffixes("|"); // doc|pdf|htm
					sb.append("var file = Utils.xtractFile(v,'" + suffixes + "');");
					sb.append("Wicket.$(" + name.getMarkupId() + ").value = file.filename + '.' + file.ext ;");
					sb.append("Wicket.$(" + choice.getMarkupId() + ").value = Utils.translateSuffix(file.ext);");
					return sb.toString() + script;
				}

			};
		}

		@Override
		protected void onEvent(AjaxRequestTarget target) {
			FileUpload fileUpload = ((FileUploadField) getComponent()).getFileUpload();
			if (fileUpload != null) {
				//target.addComponent(EditableResourceLink.this);
			}

		}
	}
	
	protected class StatusModel extends Model<String>{
        private static final long serialVersionUID = 1L;
        private String id;
	    
	    public StatusModel(String id) {
            this.id = id;
        }

        @Override
        public String getObject() {
	        if (!StringUtils.isEmpty(id) && id.equals(getAppSession().getUploadId()) ){
	            if (getAppSession().isUploading()) {
	                return getString("uploading");
	            } else if (getAppSession().isUploadComplete()) {
	                return getString("uploadcomplete");
	            } else {
	                return "";
	            }
	        } else {
	            return "";
	        }
        }
	}

	/**
	 * Constructor
	 * 
	 * @param id Wicket Id
	 * @param data Data Object for Model
	 * 
	 */
	public EditableResourceLink(String id, ILinkData data) {
		super(id);
		add(SCRIPT_UTILS);
		add(Css.CSS_BUTTONS);
		super.setOutputMarkupId(true);
		this.data = data;
		if (data == null) {
			String message = "The Constructor for AjaxEditableLink requires a value for data.";
			throw new WicketRuntimeException(message);
		}
	}

	/**
	 * Lazy initialization of the label and editor components and set tempModel
	 * to null.
	 * 
	 * @param model The model for the label and editor
	 */
	private void initLabelForm(IModel model) {
		// actual displayed Link
		add(newSharedResourceLink(data.getUrl(), "link"));
		
		StatusModel statusModel = new StatusModel(data.getId());
		AjaxSelfUpdatingLabel statusLabel = newUploadStatusLabel("uploadstatus",statusModel);
		add(statusLabel);
		
		WebMarkupContainer formDiv = new WebMarkupContainer("resource-form-div");
		formDiv.setOutputMarkupId(true);
		add(formDiv);
		
		// hidden configure form
		resourceForm = newFileUploadForm("resource-form", statusModel);
		formDiv.add(resourceForm);
		final Component name = newEditor("editor", new PropertyModel(data, "name"));
		resourceForm.add(newEditor("title-editor", new PropertyModel(data, "title")));
		resourceForm.add(new CheckBox("hide-link", new PropertyModel(data, "hideLink")));
		final Component upload = newFileUpload(this, "file-upload", new PropertyModel(data, "fileUpload"));
		final Component choice = newDropDownChoice(this, "type-select", new PropertyModel(data, "resourceType"), Arrays
				.asList(ResourceType.values()));

		// AjaxBehaviour to update fields once file is selected
		upload.add(new FileUploadBehavior("onchange", name, choice));
		resourceForm.add(name);
		resourceForm.add(upload);
		resourceForm.add(choice);

		add(new AjaxConfigureIcon("configureIcon", formDiv.getMarkupId()));

	}
	
	protected Component newSharedResourceLink(final String path, final String componentId){
        if (StringUtils.isEmpty(path)) {
            return new Label(componentId, getString("label.notconfigured"));
        }

        Component link = new ExternalLink(componentId, path){
            private static final long serialVersionUID = 1L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                tag.put("title", data.getTitle());
                super.onComponentTag(tag);
            }

            protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag) {
                if (StringUtils.isEmpty(data.getName())) {
                    replaceComponentTagBody(markupStream, openTag, defaultNullLabel());
                } else {
                    replaceComponentTagBody(markupStream, openTag, data.getName());
                }
            }

            @Override
            protected void onBeforeRender() {
                String id = data.getId();
                if (!StringUtils.isEmpty(id) && id.equals(getAppSession().getUploadId()) ){
                    if (getAppSession().isUploading()) {
                        setEnabled(false);
                    } else {
                        setEnabled(true);
                    }
                } else {
                    setEnabled(true);
                }
                super.onBeforeRender();
            }
        };
        
        link.setVisible(!data.getHideLink());
        link.setOutputMarkupId(true);
        link.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(5)));
        return link;
	}

	/**
	 * Create the Form
	 * 
	 * @param id
	 * @param statusModel 
	 * @return
	 */
	private Form newFileUploadForm(String id, final StatusModel statusModel) {
		final Form form = new Form(id) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (getAppSession().isUploading()) {
				    log.warn("File Upload cancelled. Session is busy loading another file.");
					return;
				}
				EditableResourceLink.this.onSubmit();
			}

		};
		form.add(new SubmitLink("submit"));
		form.setOutputMarkupId(true);
		form.setMaxSize(MAX_FILE_SIZE);
		return form;
	}

	/**
	 * Create a new Text Field for editing
	 * 
	 * @param parent
	 *            The parent component
	 * @param componentId
	 *            Id that should be used by the component
	 * @param model
	 *            The model
	 * @return The editor
	 */
	protected FormComponent newEditor(String componentId, IModel model) {
		final TextField nameEditor = new TextField(componentId, model);
		nameEditor.setOutputMarkupId(true);
		return nameEditor;
	}

	/**
	 * Creates Drop Down for selecting Type
	 * 
	 * @param parent
	 * @param componentId
	 * @param model
	 * @param choices
	 * @return
	 */
	protected FormComponent newDropDownChoice(MarkupContainer parent, String componentId, IModel model,
			List<ResourceType> choices) {
		IChoiceRenderer renderer = new IChoiceRenderer() {

			private static final long serialVersionUID = 1L;

			public Object getDisplayValue(Object object) {
				if (object instanceof ResourceType) {
					return ((ResourceType) object).description;
				} else {
					return object.toString();
				}
			}

			public String getIdValue(Object object, int index) {
				if (object instanceof String) {
					return ResourceType.valueOf((String) object).toString();
				} else if (object instanceof ResourceType) {
					return object.toString();
				} else {
					return Integer.toString(index);
				}
			}

		};
		final DropDownChoice choice = new DropDownChoice(componentId, model, choices, renderer);
		choice.setOutputMarkupId(true);
		choice.setNullValid(true);
		return choice;
	}

	/**
	 * Create File Upload for selecting file from desktop
	 * 
	 * @param parent
	 * @param componentId
	 * @param model
	 * @return
	 */
	protected FormComponent newFileUpload(MarkupContainer parent, String componentId, IModel model) {
		final FileUploadField upload = new FileUploadField(componentId, model) {

			private static final long serialVersionUID = 1L;

			@Override
			protected boolean forceCloseStreamsOnDetach() {
				return false; // We are going to use in Thread
			}

		};
		upload.setOutputMarkupId(true);
		//TODO set from Self updater
		//upload.setEnabled(!getAppSession().isUploading());
		return upload;
	}
	
	protected AjaxSelfUpdatingLabel newUploadStatusLabel(final String componentId, IModel model) {
		getAppSession().setUploadComplete(false);
		AjaxSelfUpdatingLabel label =  new AjaxSelfUpdatingLabel(componentId, model, 5);
		if (active){
		    label.startTimer();
		}
		return label;
	}

    /**
	 * By default this returns "onclick" uses can overwrite this on which event
	 * the label behavior should be triggered
	 * 
	 * @return The event name
	 */
	protected String getLabelAjaxEvent() {
		return "onclick";
	}

	/**
	 * @see org.apache.wicket.Component#onBeforeRender()
	 */
	protected void onBeforeRender() {
		super.onBeforeRender();
		// lazily add label and form
		if (resourceForm == null) {
			initLabelForm(getDefaultModel());
		}
	}

	/**
	 * @see org.apache.wicket.MarkupContainer#setModel(org.apache.wicket.model.IModel)
	 */
	public final Component setModel(IModel model) {
		String message = "AjaxEditableLink constructs its own model. Do not set it.";
		throw new WicketRuntimeException(message);
	}

	/**
	 * Invoked when the label is in edit mode, received a new input, but that
	 * input didn't validate
	 * 
	 * @param target
	 *            the ajax request target
	 */
	protected void onError(AjaxRequestTarget target) {
		// form is safe
	}

	protected void onSubmit() {
		// override
	}

	/**
	 * Override this to display a different value when the model object is null.
	 * Default is <code>...</code>
	 * 
	 * @return The string which should be displayed when the model object is
	 *         null.
	 */
	protected String defaultNullLabel() {
		return "...";
	}

	/**
	 * Dummy override to fix WICKET-1239
	 * 
	 * @see org.apache.wicket.Component#onModelChanged()
	 */
	protected void onModelChanged() {
		super.onModelChanged();
	}

	/**
	 * Dummy override to fix WICKET-1239
	 * 
	 * @see org.apache.wicket.Component#onModelChanging()
	 */
	protected void onModelChanging() {
		super.onModelChanging();
	}

	private IFileUploadStatus getAppSession() {
		return (IFileUploadStatus) getSession();
	}

    public void setActive(boolean active) {
        this.active = active;
    }

}
