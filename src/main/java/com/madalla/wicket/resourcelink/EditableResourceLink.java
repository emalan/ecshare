package com.madalla.wicket.resourcelink;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.MarkupContainer;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IComponentAwareHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
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
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.webapp.css.Css;
import com.madalla.webapp.upload.IFileUploadInfo;
import com.madalla.webapp.upload.IFileUploadStatus;
import com.madalla.wicket.configure.AjaxConfigureIcon;


public class EditableResourceLink extends Panel {
	private static final long serialVersionUID = 1L;
	private static Bytes MAX_FILE_SIZE = Bytes.kilobytes(5000);
	private static final Logger log = LoggerFactory.getLogger(EditableResourceLink.class);
	
	public static final ResourceReference SCRIPT_UTILS = new PackageResourceReference(EditableResourceLink.class,"resourcelink.js");

	private Form<Object> resourceForm;
	private boolean editMode;

	/** edit data **/
	private ILinkData data;

	public interface ILinkData extends Serializable {
		String getId();

		String getName();

		String getTitle();

		ResourceReference getResourceReference();

		FileUpload getFileUpload();

		LinkResourceType getResourceType();

		Boolean getHideLink();

		void setName(String name);

		void setTitle(String title);

		void setFileUpload(FileUpload upload);

		void setResourceType(LinkResourceType type);

		void setHideLink(Boolean hide);

        String getUrl();

        void setUrl(String url);

	}

	public enum LinkResourceType {

		TYPE_PDF("application/pdf", "pdf", "Adobe PDF"),
		TYPE_DOC("application/msword", "doc", "Word document");
		//TYPE_ODT("application/vnd.oasis.opendocument.text", "odt", "ODT document");

		public final String mimeType; // Mime Type
		public final String suffix; // File suffix
		public final String description;

		LinkResourceType(String mimeType, String suffix, String description) {
			this.mimeType = mimeType;
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
			for (LinkResourceType type : LinkResourceType.values()) {
				sb.append(s + type.suffix);
			}
			return sb.toString().substring(1);
		}

		public static LinkResourceType getByMimeType(final String mimeType) {
			for (LinkResourceType type : LinkResourceType.values()) {
				if (type.mimeType.equalsIgnoreCase(mimeType)) {
					return type;
				}
			}
			return null;
		}

	}
	
	private class FileUploadCallDecorator implements IAjaxCallDecorator, IComponentAwareHeaderContributor {
		private static final long serialVersionUID = 1L;
		private final String componentMarkupId;
		private final String nameMarkupId;
		private final String choiceMarkupId;
		
		public FileUploadCallDecorator(final String componentMarkupId, final String nameMarkupId, final String choiceMarkupId) {
			this.componentMarkupId = componentMarkupId;
			this.nameMarkupId = nameMarkupId;
			this.choiceMarkupId = choiceMarkupId;
		}
		
		public CharSequence decorateScript(Component component, CharSequence script) {
			StringBuffer sb = new StringBuffer("var v = Wicket.$(" + componentMarkupId + ").value;");
			String suffixes = LinkResourceType.getResourceTypeSuffixes("|"); // doc|pdf|htm
			sb.append("var file = Utils.xtractFile(v,'" + suffixes + "');");
			sb.append("Wicket.$(" + nameMarkupId + ").value = file.filename + '.' + file.ext ;");
			sb.append("Wicket.$(" + choiceMarkupId + ").value = Utils.translateSuffix(file.ext);");
			return sb.toString() + script;
		}

		public CharSequence decorateOnSuccessScript(Component component, CharSequence script) {
			return script;
		}

		public CharSequence decorateOnFailureScript(Component component, CharSequence script) {
			return script;
		}

		public void renderHead(Component component, IHeaderResponse response) {
			response.renderJavaScriptReference(SCRIPT_UTILS);
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

		@Override
		protected IAjaxCallDecorator getAjaxCallDecorator() {
			return new FileUploadCallDecorator(getComponent().getMarkupId(), name.getMarkupId(), choice.getMarkupId());
		}

		@Override
		protected void onEvent(AjaxRequestTarget target) {
			FileUpload fileUpload = ((FileUploadField) getComponent()).getFileUpload();
			if (fileUpload != null) {
				//target.addComponent(EditableResourceLink.this);
			}

		}
		
		@Override
		public void renderHead(Component component, IHeaderResponse response) {
			response.renderJavaScriptReference(SCRIPT_UTILS);
			StringBuffer sb = new StringBuffer();
			sb.append("Utils.translateSuffix = function(suffix){");
			for (LinkResourceType type : LinkResourceType.values()) {
				sb.append("if (suffix.toLowerCase() == '" + type.suffix + "') return '" + type + "';");
			}
			sb.append("return '';");
			sb.append("};");

			response.renderJavaScript(sb.toString(), "translateSuffix");
			super.renderHead(component, response);
		}
	}

	protected class StatusModel extends Model<String>{
        private static final long serialVersionUID = 1L;
        private String id;
        private String status;

	    public void setStatus(String status) {
			this.status = status;
		}

		public StatusModel(String id) {
            this.id = id;
        }

        @Override
        public String getObject() {
        	//log.debug("StatusModel - checking status for id=" + id);
        	if (StringUtils.isNotEmpty(status)){
        		return status;
        	}
        	Boolean fileUploading = isFileUploading(id);
	        if ( fileUploading == null) {
	        	return "";
	        } else if (fileUploading) {
	        	return getString("uploading");
	        } else {
	        	return getString("uploadcomplete");
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
		super.setOutputMarkupId(true);
		this.data = data;
		if (data == null) {
			String message = "The Constructor for AjaxEditableLink requires a value for data.";
			throw new WicketRuntimeException(message);
		}
		
	}
	
    @Override
    public void renderHead(IHeaderResponse response) {
    	super.renderHead(response);
    	response.renderCSSReference(Css.CSS_BUTTONS);
    }

	/**
	 * Lazy initialization of the label and editor components and set tempModel
	 * to null.
	 *
	 * @param model The model for the label and editor
	 */
	private void initLabelForm(IModel<?> model) {

		WebMarkupContainer displayArea = new WebMarkupContainer("displayArea");
		add(displayArea);

		// display : link and feedback
		StatusModel statusModel = new StatusModel(data.getId());
		Component statusLabel = new Label("uploadstatus",statusModel);
		statusLabel.setOutputMarkupId(true);
		displayArea.add(statusLabel);
		displayArea.add(newSharedResourceLink("link", data, statusLabel));

		WebMarkupContainer formDiv = new WebMarkupContainer("resource-form-div");
		formDiv.setOutputMarkupId(true);
		add(formDiv);

		// hidden configure form
		resourceForm = newFileUploadForm("resource-form", statusModel);
		formDiv.add(resourceForm);
		final Component name = newEditor("editor", new PropertyModel<String>(data, "name"));
		resourceForm.add(newEditor("title-editor", new PropertyModel<String>(data, "title")));
		resourceForm.add(new CheckBox("hide-link", new PropertyModel<Boolean>(data, "hideLink")));
		
		//TODO make use of this for multiple uploads
		List<FileUpload> fileUploads = new ArrayList<FileUpload>();
		fileUploads.add(data.getFileUpload());
		final Component upload = newFileUpload(this, "file-upload", new ListModel<FileUpload>(fileUploads));
		upload.setOutputMarkupId(true);
		final Component choice = newDropDownChoice(this, "type-select", new PropertyModel<String>(data, "resourceType"), Arrays
				.asList(LinkResourceType.values()));

		// AjaxBehaviour to update fields once file is selected
		upload.add(new FileUploadBehavior("onchange", name, choice));
		resourceForm.add(name);
		resourceForm.add(upload);
		resourceForm.add(choice);

		add(new AjaxConfigureIcon("configureIcon", displayArea, formDiv, 17));

	}

    protected Component newSharedResourceLink(final String id, final ILinkData data, final Component feedback ){

    	//href Model
    	IModel<String> hrefModel = new Model<String>(){
			private static final long serialVersionUID = 1L;

    		@Override
			public String getObject() {
				if (StringUtils.isEmpty(data.getUrl())){
					return "#";
				} else {
					return data.getUrl();
				}
			}

    	};

    	//label model
    	IModel<String> labelModel = new Model<String>(){
			private static final long serialVersionUID = 1L;

			@Override
			public String getObject() {
				if (data.getHideLink() != null && data.getHideLink()){
					return getString("label.hidden");
				} else if (StringUtils.isEmpty(data.getName())) {
					return getString("label.notconfigured");
                } else {
                	return data.getName();
                }
			}

    	};

        Component link = new IndicatingUploadLink(id, data,  hrefModel, labelModel){
			private static final long serialVersionUID = 1L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                tag.put("title", data.getTitle());
                super.onComponentTag(tag);
            }

            @Override
            protected void onBeforeRender() {
            	//log.debug("sharedresourceLink - checking status for id=" + data.getId());

            	if (data == null){
            		log.error("data is null");
            	}

            	if(!editMode && data.getHideLink() != null && data.getHideLink().equals(Boolean.TRUE)){
            		setVisible(false);
            	} else {
            		setVisible(true);
            	}

            	if (StringUtils.isEmpty(data.getUrl()) || (isFileUploading(data.getId()) != null && isFileUploading(data.getId()))) {
                    setEnabled(false);
                } else {
                    setEnabled(true);
                }

            	super.onBeforeRender();
            }

        };

        link.setVisibilityAllowed(true);
        link.setOutputMarkupId(true);
//        link.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(6)){
//
//			private static final long serialVersionUID = 1L;
//
//			@Override
//			protected void onPostProcessTarget(AjaxRequestTarget target) {
//				target.addComponent(feedback);
//				String indicatorId = getIndicator();
//				if (isFileUploading(data.getId()) != null && isFileUploading(data.getId())){
//					target.appendJavaScript("wicketShow('" + indicatorId +"');");
//				} else {
//					target.appendJavaScript("wicketHide('" + indicatorId +"');");
//				}
//
//			}
//
//			protected String getIndicator()
//			{
//				if (getComponent() instanceof IndicatingUploadLink)
//				{
//					return ((IndicatingUploadLink)getComponent()).getAjaxIndicatorMarkupId();
//				}
//				return null;
//			}
//
//
//        });
        return link;
	}
    
	/**
	 * Create the Form
	 *
	 * @param id
	 * @param statusModel
	 * @return
	 */
	private Form<Object> newFileUploadForm(final String id, final StatusModel statusModel) {
		final Form<Object> form = new Form<Object>(id) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				if (isFileUploading(id) != null && isFileUploading(id)) {
				    log.warn("File Upload cancelled. Session is busy uploading file.");
					return;
				}
				if (data.getFileUpload() != null){
					if (!validateFile(data.getFileUpload().getClientFileName())){
						statusModel.setStatus("Selected file invalid.");
						return;
					}
				}
				EditableResourceLink.this.onSubmit();
			}

		};
		form.add(new SubmitLink("submit"));
		form.setOutputMarkupId(true);
		form.setMaxSize(MAX_FILE_SIZE);
		return form;
	}

	private boolean validateFile(String fileName){
		for (LinkResourceType type : LinkResourceType.values()) {
			if (StringUtils.endsWith(fileName, type.suffix)){
				return true;
			}
		}
		return false;
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
	protected FormComponent<String> newEditor(String componentId, IModel<String> model) {
		final TextField<String> nameEditor = new TextField<String>(componentId, model);
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
	@SuppressWarnings("unchecked") //this seems safe and the best way to implement
    protected FormComponent<String> newDropDownChoice(MarkupContainer parent, String componentId, IModel<String> model,
			List<LinkResourceType> choices) {

	    @SuppressWarnings("rawtypes")
		IChoiceRenderer renderer = new IChoiceRenderer() {
			private static final long serialVersionUID = 1L;

			public Object getDisplayValue(Object object) {
				if (object instanceof LinkResourceType) {
					return ((LinkResourceType) object).description;
				} else {
					return object.toString();
				}
			}

			public String getIdValue(Object object, int index) {
				if (object instanceof String) {
					return LinkResourceType.valueOf((String) object).toString();
				} else if (object instanceof LinkResourceType) {
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
	protected FormComponent<List<FileUpload>> newFileUpload(MarkupContainer parent, String componentId, IModel<List<FileUpload>> model) {
		final FileUploadField upload = new FileUploadField(componentId, model) {

			private static final long serialVersionUID = 1L;

			@Override
			protected boolean forceCloseStreamsOnDetach() {
				return false; // We are going to use in Thread
			}

		};
		upload.setOutputMarkupId(true);
		return upload;
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
	@Override
	protected void onBeforeRender() {
		super.onBeforeRender();
		// lazily add label and form
		if (resourceForm == null) {
			initLabelForm(getDefaultModel());
		}
	}


	@Override
    public MarkupContainer setDefaultModel(IModel<?> model) {
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
	@Override
	protected void onModelChanged() {
		super.onModelChanged();
	}

	/**
	 * Dummy override to fix WICKET-1239
	 *
	 * @see org.apache.wicket.Component#onModelChanging()
	 */
	@Override
	protected void onModelChanging() {
		super.onModelChanging();
	}

	public Boolean isFileUploading(String id) {
		if (StringUtils.isEmpty(id)) {
			return null;
		}
		IFileUploadStatus status = ((IFileUploadInfo) getSession()).getFileUploadStatus(id);
		if (status == null ) {
			return null;
		} else if (status.isUploading()) {
			return true;
		} else {
			return false;
		}
	}

	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

}
