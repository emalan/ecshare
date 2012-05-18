package com.madalla.webapp.cms;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.resource.ResourceReference;
import org.emalan.cms.IDataService;
import org.emalan.cms.bo.page.PageData;
import org.emalan.cms.bo.page.ResourceData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.webapp.CmsPanel;
import com.madalla.webapp.css.Css;
import com.madalla.webapp.upload.FileUploadThread;
import com.madalla.webapp.upload.IFileUploadInfo;
import com.madalla.webapp.upload.IFileUploadProcess;
import com.madalla.wicket.resourcelink.EditableResourceLink;
import com.madalla.wicket.resourcelink.EditableResourceLink.ILinkData;
import com.madalla.wicket.resourcelink.EditableResourceLink.LinkResourceType;

/**
 * Editable Link to a Respository Resource.
 * <p>
 * A link that will display a Repository Resource such as a PDF or a Word Doc.
 * The Resource Link is editable when a valid user is logged. The following is
 * configurable:
 * <ul>
 * <li>Upload resource to Repository from desktop.</li>
 * <li>Change File Name that is displayed</li>
 * <li>Edit hover for link</li>
 * <li>Hide or show the link resource</li>
 * </ul>
 * 
 * </p>
 * 
 * @author Eugene Malan
 * 
 */
public class ContentLinkPanel extends CmsPanel {
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(ContentLinkPanel.class);

    /**
     * This is used to pass data to
     * {@link com.madalla.wicket.resourcelink.EditableResourceLink}
     * 
     * @author Eugene Malan
     * 
     */
    public class LinkData implements ILinkData {

        private static final long serialVersionUID = 1L;

        private String id;
        private String name;
        private String title;
        private ResourceReference resourceReference;
        private transient FileUpload fileUpload;
        private LinkResourceType resourceType;
        private Boolean hideLink;
        private String url;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }

        public void setResourceReference(ResourceReference resourceReference) {
            this.resourceReference = resourceReference;
        }

        public ResourceReference getResourceReference() {
            return resourceReference;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setFileUpload(FileUpload fileUpload) {
            this.fileUpload = fileUpload;
        }

        public FileUpload getFileUpload() {
            return fileUpload;
        }

        public LinkResourceType getResourceType() {
            return resourceType;
        }

        public void setResourceType(LinkResourceType resourceType) {
            this.resourceType = resourceType;
        }

        public Boolean getHideLink() {
            return hideLink;
        }

        public void setHideLink(Boolean hideLink) {
            this.hideLink = hideLink;
        }

        @Override
        public String toString() {
            return ReflectionToStringBuilder.toString(this).toString();
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }

    public ContentLinkPanel(final String id, final String nodeName) {
        super(id);

        log.debug("Editable Link Panel being created for node=" + nodeName + " id=" + id);

        PageData page = getRepositoryService().getPage(nodeName);
        final ResourceData resourceData = getRepositoryService().getContentResource(page, id);
        log.debug("retrieved Resource data. " + resourceData);
        final LinkData linkData = createView(resourceData);
        if (!StringUtils.isEmpty(resourceData.getUrl())) {
            ContentSharedResource.registerResource((WebApplication) getApplication(), linkData, resourceData.getUrl(),
                    getRepositoryService());
        }

        final EditableResourceLink editableLink = new EditableResourceLink("contentLink", linkData) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void onBeforeRender() {
                if (((IContentAdmin) getSession()).isLoggedIn()) {
                    setEditMode(true);
                } else {
                    setEditMode(false);
                }
                super.onBeforeRender();
            }

            @Override
            protected void processSubmit(ILinkData data) {
                log.trace("onSubmit called");
                String mountPath = "";

                IFileUploadProcess process = new ContentLinkUploadProcess(getRepositoryService(), data);

                if (data.getFileUpload() != null) {
                    mountPath = ContentSharedResource.registerResource((WebApplication) getApplication(), data,
                            getRepositoryService());
                    data.setUrl(mountPath);

                    // Start separate thread so upload can continue if user
                    // navigates away
                    // final SubmitThread it = new SubmitThread(getAppSession(),
                    // linkData, getRepositoryService());
                    // it.start();

                    IFileUploadInfo uploadInfo = (IFileUploadInfo) getSession();

                    final Thread submit = new FileUploadThread(uploadInfo, data.getFileUpload(), process, data.getId());
                    submit.start();

                } else {
                    log.debug("onSubmit - setting InputStream to null");
                    process.execute(null, "");
                }
            }

            @Override
            protected IFileUploadInfo getFileUploadInfo() {
                return (IFileUploadInfo) getSession();
            }

        };
        editableLink.add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
            private static final long serialVersionUID = -3131361470864509715L;

            @Override
            public String getObject() {
                String cssClass;
                if (((IContentAdmin) getSession()).isLoggedIn()) {
                    cssClass = "contentLinkEdit";
                } else {
                    cssClass = "contentLink";
                }
                return cssClass;
            }
        }));
        add(editableLink);
    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.renderCSSReference(Css.CSS_FORM);
    }

    private LinkData createView(final ResourceData resourceData) {
        LinkData linkData = new LinkData();
        linkData.setId(resourceData.getId());
        linkData.setName(resourceData.getUrlDisplay());
        linkData.setTitle(resourceData.getUrlTitle());
        LinkResourceType type = LinkResourceType.getByMimeType(resourceData.getType());
        // TODO handle null here
        linkData.setResourceType(type);
        linkData.setHideLink(resourceData.getHideLink());
        linkData.setUrl(resourceData.getUrl());
        return linkData;
    }

    private class ContentLinkUploadProcess implements IFileUploadProcess {

        final private IDataService service;
        final private ILinkData linkData;

        public ContentLinkUploadProcess(IDataService service, ILinkData linkData) {
            this.service = service;
            this.linkData = linkData;
        }

        public void execute(InputStream inputStream, String id) {

            ResourceData resourceData = service.getContentResource(linkData.getId());
            resourceData.setUrl(linkData.getUrl());

            resourceData.setInputStream(inputStream);
            if (StringUtils.isEmpty(linkData.getName())) {
                linkData.setName(id);
            }

            resourceData.setUrlDisplay(linkData.getName());
            resourceData.setUrlTitle(linkData.getTitle());
            resourceData.setType(linkData.getResourceType().mimeType);
            resourceData.setHideLink(linkData.getHideLink());

            service.saveContentResource(resourceData);

        }

    }

}
