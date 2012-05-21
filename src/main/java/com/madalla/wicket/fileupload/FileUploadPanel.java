package com.madalla.wicket.fileupload;

import java.io.IOException;
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
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.validator.AbstractValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.webapp.scripts.JavascriptResources;

public abstract class FileUploadPanel extends Panel {

    private static final long serialVersionUID = 1L;
    private static Bytes MAX_FILE_SIZE = Bytes.kilobytes(5000);
    private static final Logger log = LoggerFactory.getLogger(FileUploadPanel.class);

    public abstract class ResourceForm extends Form<ILinkData> {
        private static final long serialVersionUID = 1L;

        private FileUploadField fileUploadField;

        public ResourceForm(final String id, final IModel<ILinkData> model) {
            super(id, model);

            fileUploadField = new FileUploadField("fileUpload") {

                private static final long serialVersionUID = 1L;

                @Override
                protected boolean forceCloseStreamsOnDetach() {
                    return false; // we want to upload stream in Thread
                }

            };
            fileUploadField.setRequired(true);
            add(fileUploadField);

            final Component name = new RequiredTextField<String>("fileName", new PropertyModel<String>(
                    model.getObject(), "name")).setOutputMarkupId(true);
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

            add(new TextField<String>("fileTitle", new PropertyModel<String>(model.getObject(), "title"))
                    .setOutputMarkupId(true));

            add(new CheckBox("hideLink", new PropertyModel<Boolean>(model.getObject(), "hideLink")));

            final Component choice = newDropDownChoice(this, "fileType", new PropertyModel<String>(model.getObject(),
                    "resourceType"), Arrays.asList(FileUploadTypeType.values()));
            add(choice);

            final FeedbackPanel uploadFeedback = new FeedbackPanel("uploadFeedback");
            uploadFeedback.setOutputMarkupId(true);
            uploadFeedback.setMaxMessages(1);
            add(uploadFeedback);

            final Label selected = new Label("selected");
            selected.setOutputMarkupId(true);
            add(selected);

            // Ajax process file selection
            fileUploadField.add(new FileUploadBehavior("onchange", name, choice, selected));

            add(new IndicatingAjaxButton("submit") {
                private static final long serialVersionUID = 1L;

                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    log.trace("Ajax onSubmit. " + model.getObject());
                    target.add(uploadFeedback);
                    ResourceForm.this.refreshDisplay(target);
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
            setMultiPart(true);
        }

        @Override
        protected void onSubmit() {
            ILinkData modelObj = getModelObject();

            // Fileupload
            List<FileUpload> uploads = fileUploadField.getFileUploads();
            if (uploads == null || uploads.size() == 0) {
                log.error("File Upload is null.");
                error(getString("error.fail"));
                return;
            } else {
                FileUpload fileUpload = uploads.get(0);
                final String fileName = fileUpload.getClientFileName();
                if (fileName == null || !validateFile(fileName)) {
                    error(getString("error.invalidFile"));
                    return;
                } else {
                    modelObj.setFileName(fileName);
                    try {
                        modelObj.setFileInputStream(fileUpload.getInputStream());
                    } catch (IOException e) {
                        log.error("Exception while getting InputStream.", e);
                        error(getString("error.invalidFile"));
                        return;
                    }
                }
            }

            log.trace("onSubmit - " + modelObj);

            if (isFileUploading()) {
                log.warn("File Upload cancelled. Session is busy uploading file.");
                error(getString("error.fileUploading"));
                return;
            }

            info(getString("info.submit"));
            processSubmit(modelObj);
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
        protected FormComponent<String> newDropDownChoice(MarkupContainer parent, String componentId,
                IModel<String> model, List<FileUploadTypeType> choices) {

            @SuppressWarnings("rawtypes")
            IChoiceRenderer renderer = new IChoiceRenderer() {
                private static final long serialVersionUID = 1L;

                public Object getDisplayValue(Object object) {
                    if (object instanceof FileUploadTypeType) {
                        return ((FileUploadTypeType) object).description;
                    } else {
                        return object.toString();
                    }
                }

                public String getIdValue(Object object, int index) {
                    if (object instanceof String) {
                        return FileUploadTypeType.valueOf((String) object).toString();
                    } else if (object instanceof FileUploadTypeType) {
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

        private boolean validateFile(String fileName) {
            for (FileUploadTypeType type : FileUploadTypeType.values()) {
                if (StringUtils.endsWith(fileName, type.suffix)) {
                    return true;
                }
            }
            return false;
        }

        abstract void refreshDisplay(AjaxRequestTarget target);

        abstract void processSubmit(ILinkData iLinkData);

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
                    String suffixes = FileUploadTypeType.getResourceTypeSuffixes("|"); // doc|pdf|htm
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
            response.renderJavaScriptReference(JavascriptResources.SCRIPT_UTILS);

            // translate the suffix to a value in dropdown if possible
            StringBuffer sb = new StringBuffer();
            sb.append("Utils.translateSuffix = function(suffix){");
            for (FileUploadTypeType type : FileUploadTypeType.values()) {
                sb.append("if (suffix != null && suffix.toLowerCase() == '" + type.suffix + "') return '" + type + "';");
            }
            sb.append("return '';");
            sb.append("};");

            response.renderJavaScript(sb.toString(), "translateSuffix");
            super.renderHead(component, response);
        }
    }

    private final ILinkData data;
    private Form<ILinkData> resourceForm;

    public FileUploadPanel(final String id, final ILinkData data) {
        super(id);
        super.setOutputMarkupId(true);
        this.data = data;

        if (data == null) {
            String message = "The Constructor for FileUploadPanel requires a value for data.";
            throw new WicketRuntimeException(message);
        }

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

    /**
     * Lazy initialization of the Form
     * 
     * @param model
     *            The model for the form components
     */
    private void initLabelForm(IModel<ILinkData> model) {

        WebMarkupContainer formDiv = new WebMarkupContainer("resource-form-div");
        formDiv.setOutputMarkupId(true);
        add(formDiv);

        // hidden configure form
        final String formId = "resource-form";
        resourceForm = new ResourceForm(formId, model) {
            private static final long serialVersionUID = 1L;

            @Override
            void refreshDisplay(AjaxRequestTarget target) {
                FileUploadPanel.this.refreshDisplay(target);
            }

            @Override
            void processSubmit(ILinkData iLinkData) {
                FileUploadPanel.this.processUpload(iLinkData);
            }

        };
        formDiv.add(resourceForm);

    }

    protected abstract boolean isFileUploading();

    protected abstract void processUpload(ILinkData data);
    
    /**
     * Overide if you need to refresh any components after upload
     * 
     * @param target
     */
    protected void refreshDisplay(AjaxRequestTarget target) {
        
    }

}
