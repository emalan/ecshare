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
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.util.ListModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.webapp.css.Css;
import com.madalla.webapp.scripts.JavascriptResources;
import com.madalla.webapp.upload.IFileUploadInfo;
import com.madalla.webapp.upload.IFileUploadStatus;
import com.madalla.wicket.configure.AjaxConfigureIcon;

public class EditableResourceLink extends Panel {
    private static final long serialVersionUID = 1L;
    private static Bytes MAX_FILE_SIZE = Bytes.kilobytes(5000);
    private static final Logger log = LoggerFactory.getLogger(EditableResourceLink.class);
    public static final ResourceReference SCRIPT_UTILS = new PackageResourceReference(EditableResourceLink.class,
            "resourcelink.js");

    private Form<ILinkData> resourceForm;
    private boolean editMode;

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
        
        String getStatus();
        
        void setStatus(String status);

    }

    public enum LinkResourceType {

        TYPE_PDF("application/pdf", "pdf", "Adobe PDF"), TYPE_DOC("application/msword", "doc", "Word document");
        // TYPE_ODT("application/vnd.oasis.opendocument.text", "odt",
        // "ODT document");

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

    public abstract class ResourceForm extends Form<ILinkData> {
        private static final long serialVersionUID = 1L;

        public ResourceForm(final String id, final IModel<ILinkData> model) {
            super(id, model);

            final Component name = new RequiredTextField<String>("editor", new PropertyModel<String>(model.getObject(), "name"))
                    .setOutputMarkupId(true);
            name.add(new AbstractValidator<String>() {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onValidate(IValidatable<String> validatable) {
                    if (validatable.getValue().equals("invalid")) {
                        error(validatable, "error.invalidFile");
                    }
                }
                
            });
            add(name);
            
            add(new TextField<String>("title-editor", new PropertyModel<String>(model.getObject(), "title"))
                    .setOutputMarkupId(true));
            
            add(new CheckBox("hide-link", new PropertyModel<Boolean>(model.getObject(), "hideLink")));

            final Component upload = newFileUpload(this, "file-upload", model.getObject().getFileUpload());
            upload.setOutputMarkupId(true);
            add(upload);
            
            final Component choice = newDropDownChoice(this, "type-select", new PropertyModel<String>(model.getObject(),
                    "resourceType"), Arrays.asList(LinkResourceType.values()));
            add(choice);

            final Component uploadFeedback = new FeedbackPanel("uploadFeedback");
            uploadFeedback.setOutputMarkupId(true);
            add(uploadFeedback);
            
            final Label selected = new Label("selected");
            selected.setOutputMarkupId(true);
            add(selected);

            // Ajax process file selection
            upload.add(new FileUploadBehavior("onchange", name, choice, selected));
            


            add(new IndicatingAjaxButton("submit") {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    log.trace("Ajax onSubmit. " + model.getObject());
                    target.add(uploadFeedback);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                    log.trace("Ajax onSubmit. " + model.getObject());
                    target.add(uploadFeedback);
                }
            });
        }

        @Override
        protected void onConfigure() {
            setOutputMarkupId(true);
            setMaxSize(MAX_FILE_SIZE);
        }

        @Override
        protected void onSubmit() {
            log.trace("onSubmit - " + getModelObject());
            if (isFileUploading(getId()) != null && isFileUploading(getId())) {
                log.warn("File Upload cancelled. Session is busy uploading file.");
                return;
            }
            if (getModelObject().getFileUpload() != null) {
                if (!validateFile(getModelObject().getFileUpload().getClientFileName())) {
                    getModelObject().setStatus("Selected file invalid.");
                    return;
                }
            }
            //EditableResourceLink.this.onSubmit();

        }
        
        abstract void refreshDisplay(AjaxRequestTarget target);

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
        final Component selected;

        public FileUploadBehavior(final String event, final Component name, final Component choice, final Label selected) {
            super(event);
            this.name = name;
            this.choice = choice;
            this.selected = selected;
        }

        @Override
        protected IAjaxCallDecorator getAjaxCallDecorator() {
            return new IAjaxCallDecorator() {
                private static final long serialVersionUID = 1L;

                public CharSequence decorateScript(Component component, CharSequence script) {
                    StringBuffer sb = new StringBuffer("var v = Wicket.$(" + getComponent().getMarkupId() + ").value;");
                    String suffixes = LinkResourceType.getResourceTypeSuffixes("|"); // doc|pdf|htm
                    sb.append("var file = Utils.xtractFile(v,'" + suffixes + "');");
                    sb.append("var display = file.filename + '.' + file.ext;");
                    sb.append("Utils.setTextContent(Wicket.$('" + selected.getMarkupId() + "'),display) ;");
                    sb.append("Wicket.$(" + name.getMarkupId() + ").value = display ;");
                    sb.append("Wicket.$(" + choice.getMarkupId() + ").value = Utils.translateSuffix(file.ext);");
                    return sb.toString() + script;
                }
                
                public CharSequence decorateOnSuccessScript(Component component, CharSequence script) {
                    return script;
                }
                
                public CharSequence decorateOnFailureScript(Component component, CharSequence script) {
                    return script;
                }
            };
        }

        @Override
        protected void onEvent(AjaxRequestTarget target) {
            FileUpload fileUpload = ((FileUploadField) getComponent()).getFileUpload();
            if (fileUpload != null) {
                log.trace("onEvent - " + fileUpload.getClientFileName());
                // target.addComponent(EditableResourceLink.this);
            }

        }

        @Override
        public void renderHead(Component component, IHeaderResponse response) {
            response.renderJavaScriptReference(SCRIPT_UTILS);
            
            //translate the suffix to a value in dropdown if possible
            StringBuffer sb = new StringBuffer();
            sb.append("Utils.translateSuffix = function(suffix){");
            for (LinkResourceType type : LinkResourceType.values()) {
                sb.append("if (suffix != null && suffix.toLowerCase() == '" + type.suffix + "') return '" + type + "';");
            }
            sb.append("return '';");
            sb.append("};");

            response.renderJavaScript(sb.toString(), "translateSuffix");
            super.renderHead(component, response);
        }
    }

    protected class StatusModel extends Model<String> {
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
            // log.debug("StatusModel - checking status for id=" + id);
            if (StringUtils.isNotEmpty(status)) {
                return status;
            }
            Boolean fileUploading = isFileUploading(id);
            if (fileUploading == null) {
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
     * @param id
     *            Wicket Id
     * @param data
     *            Data Object for Model
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
     * @param model
     *            The model for the label and editor
     */
    private void initLabelForm(IModel<ILinkData> model) {

        WebMarkupContainer displayArea = new WebMarkupContainer("displayArea");
        add(displayArea);

        // display : link and feedback
        final StatusModel statusModel = new StatusModel(data.getId());
        final Component statusLabel = new Label("uploadstatus", statusModel);
        statusLabel.setOutputMarkupId(true);
        displayArea.add(statusLabel);
        displayArea.add(newSharedResourceLink("link", data, statusLabel));

        WebMarkupContainer formDiv = new WebMarkupContainer("resource-form-div");
        formDiv.setOutputMarkupId(true);
        add(formDiv);

        // hidden configure form
        final String formId = "resource-form";
        resourceForm = new ResourceForm(formId, model) {
            private static final long serialVersionUID = 1L;

            @Override
            void refreshDisplay(AjaxRequestTarget target) {
                // TODO Auto-generated method stub
                
            }

        };
        formDiv.add(resourceForm);

        add(new AjaxConfigureIcon("configureIcon", displayArea, formDiv, 17));

    }

    protected Component newSharedResourceLink(final String id, final ILinkData data, final Component feedback) {

        // href Model
        IModel<String> hrefModel = new Model<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                if (StringUtils.isEmpty(data.getUrl())) {
                    return "#";
                } else {
                    return data.getUrl();
                }
            }

        };

        // label model
        IModel<String> labelModel = new Model<String>() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getObject() {
                if (data.getHideLink() != null && data.getHideLink()) {
                    return getString("label.hidden");
                } else if (StringUtils.isEmpty(data.getName())) {
                    return getString("label.notconfigured");
                } else {
                    return data.getName();
                }
            }

        };

        Component link = new IndicatingUploadLink(id, data, hrefModel, labelModel) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                tag.put("title", data.getTitle());
                super.onComponentTag(tag);
            }

            @Override
            protected void onBeforeRender() {
                // log.debug("sharedresourceLink - checking status for id=" +
                // data.getId());

                if (data == null) {
                    log.error("data is null");
                }

                if (!editMode && data.getHideLink() != null && data.getHideLink().equals(Boolean.TRUE)) {
                    setVisible(false);
                } else {
                    setVisible(true);
                }

                if (StringUtils.isEmpty(data.getUrl())
                        || (isFileUploading(data.getId()) != null && isFileUploading(data.getId()))) {
                    setEnabled(false);
                } else {
                    setEnabled(true);
                }

                super.onBeforeRender();
            }

        };

        link.setVisibilityAllowed(true);
        link.setOutputMarkupId(true);
        // link.add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(6)){
        //
        // private static final long serialVersionUID = 1L;
        //
        // @Override
        // protected void onPostProcessTarget(AjaxRequestTarget target) {
        // target.addComponent(feedback);
        // String indicatorId = getIndicator();
        // if (isFileUploading(data.getId()) != null &&
        // isFileUploading(data.getId())){
        // target.appendJavaScript("wicketShow('" + indicatorId +"');");
        // } else {
        // target.appendJavaScript("wicketHide('" + indicatorId +"');");
        // }
        //
        // }
        //
        // protected String getIndicator()
        // {
        // if (getComponent() instanceof IndicatingUploadLink)
        // {
        // return
        // ((IndicatingUploadLink)getComponent()).getAjaxIndicatorMarkupId();
        // }
        // return null;
        // }
        //
        //
        // });
        return link;
    }

    private boolean validateFile(String fileName) {
        for (LinkResourceType type : LinkResourceType.values()) {
            if (StringUtils.endsWith(fileName, type.suffix)) {
                return true;
            }
        }
        return false;
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
    @SuppressWarnings("unchecked")
    // this seems safe and the best way to implement
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
        @SuppressWarnings("rawtypes")   
        final DropDownChoice choice = new DropDownChoice(componentId, model, choices, renderer);
        choice.setOutputMarkupId(true);
        choice.setNullValid(false);
        choice.setRequired(true);
        return choice;
    }

    /**
     * Create File Upload for selecting file from desktop
     * 
     * @param parent
     * @param componentId
     * @param fileUpload
     * @return
     */
    protected FormComponent<List<FileUpload>> newFileUpload(MarkupContainer parent, String componentId,
            FileUpload fileUpload) {
        
        final List<FileUpload> fileUploads = new ArrayList<FileUpload>();
        fileUploads.add(fileUpload);
        
        final FileUploadField upload = new FileUploadField(componentId, new ListModel<FileUpload>(fileUploads)) {

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
            initLabelForm(new Model<ILinkData>(data));
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
        if (status == null) {
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
