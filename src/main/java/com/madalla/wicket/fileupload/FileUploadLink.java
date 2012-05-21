package com.madalla.wicket.fileupload;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.Component;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.AjaxSelfUpdatingTimerBehavior;
import org.apache.wicket.extensions.ajax.markup.html.AjaxIndicatorAppender;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.util.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class FileUploadLink extends Panel {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(FileUploadLink.class);

    private class StatusModel extends Model<String> {
        private static final long serialVersionUID = 1L;

        @Override
        public String getObject() {
            if (isFileUploading()) {
                return getString("uploading");
            } else {
                return "";
            }
        }
    }

    private final AjaxIndicatorAppender indicatorAppender = new AjaxIndicatorAppender();
    private boolean editMode;

    public FileUploadLink(final String id, final ILinkData data) {
        super(id);

        if (data == null) {
            throw new WicketRuntimeException("Data Parameter cannot be null.");
        }

        WebMarkupContainer displayArea = new WebMarkupContainer("fileUploadLink");
        add(displayArea);

        final StatusModel statusModel = new StatusModel();
        final Component statusLabel = new Label("uploadstatus", statusModel);
        statusLabel.setOutputMarkupId(true);
        displayArea.add(statusLabel);

        // resource link
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

        final Component link = new ExternalLink("link", hrefModel, labelModel) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onComponentTag(ComponentTag tag) {
                tag.put("title", data.getTitle());
                super.onComponentTag(tag);
            }

            @Override
            protected void onConfigure() {
                super.onConfigure();

                if (!editMode && data.getHideLink() != null && data.getHideLink().equals(Boolean.TRUE)) {
                    setVisible(false);
                } else {
                    setVisible(true);
                }

                if (StringUtils.isEmpty(data.getUrl()) || (isFileUploading())) {
                    setEnabled(false);
                } else {
                    setEnabled(true);
                }

            }

            @Override
            protected void onBeforeRender() {
                super.onBeforeRender();
            }

        };

        link.setVisibilityAllowed(true);
        link.setOutputMarkupId(true);
        displayArea.add(link);
        
        add(indicatorAppender);

        // Ajax indicator
        add(new AjaxSelfUpdatingTimerBehavior(Duration.seconds(6)) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void onPostProcessTarget(AjaxRequestTarget target) {
                log.trace("onPostProcessTarget");
                target.add(statusLabel);
                target.add(link);
                String indicatorId = getIndicator();
                if (isFileUploading()) {
                    log.trace("onPostProcessTarget - file uploading");
                    target.appendJavaScript("wicketShow('" + indicatorId + "');");
                } else {
                    log.trace("onPostProcessTarget - file not uploading");
                    target.appendJavaScript("wicketHide('" + indicatorId + "');");
                }

            }

            protected String getIndicator() {
                return indicatorAppender.getMarkupId();
            }

        });
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }

    protected abstract boolean isFileUploading();

}
