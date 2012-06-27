package com.madalla.webapp.cms;

import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.protocol.http.WebApplication;
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
import com.madalla.webapp.upload.IFileUploadStatus;
import com.madalla.wicket.configure.AjaxConfigureIcon;
import com.madalla.wicket.fileupload.FileUploadLink;
import com.madalla.wicket.fileupload.FileUploadPanel;
import com.madalla.wicket.fileupload.FileUploadTypeType;
import com.madalla.wicket.fileupload.ILinkData;

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

    private class ContentLinkUploadProcess implements IFileUploadProcess {

        private static final long serialVersionUID = 1L;
        
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
        
        final FileUploadLink uploadLink = new FileUploadLink("uploadLink", linkData){

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
            protected boolean isFileUploading() {
                IFileUploadStatus status = getFileUploadInfo().getFileUploadStatus(linkData.getId());
                return (status != null && status.isUploading());
            }
            
        };
        uploadLink.setOutputMarkupId(true);
        
        add(uploadLink);

        FileUploadPanel uploadForm = new FileUploadPanel("fileUpload", linkData) {
            private static final long serialVersionUID = 1L;

            @Override
            protected void processUpload(final ILinkData data) {
                log.trace("onSubmit called");
                String mountPath = "";

                IFileUploadProcess process = new ContentLinkUploadProcess(getRepositoryService(), data);

                mountPath = ContentSharedResource.registerResource((WebApplication) getApplication(), data,
                        getRepositoryService());
                data.setUrl(mountPath);

                IFileUploadInfo uploadInfo = (IFileUploadInfo) getSession();

                final Thread submit = new FileUploadThread(uploadInfo, data.getInputStream(), process, linkData.getId());
                submit.start();

            }

            @Override
            protected boolean isFileUploading() {
                IFileUploadStatus status = getFileUploadInfo().getFileUploadStatus(linkData.getId());
                return (status != null && status.isUploading());
            }

            @Override
            protected void refreshDisplay(AjaxRequestTarget target) {
                System.out.println("refreshDisplay");
                target.add(uploadLink);
            }
        };
        add(uploadForm);
        
        add(new AjaxConfigureIcon("configureIcon", uploadLink, uploadForm, 18));

        add(new AttributeModifier("class", new AbstractReadOnlyModel<String>() {
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

    }

    @Override
    public void renderHead(IHeaderResponse response) {
        response.render(CssHeaderItem.forReference(Css.CSS_FORM));
    }
    
    @Override
    protected void onConfigure() {
        super.onConfigure();
        setOutputMarkupId(true);
    }
    
    private IFileUploadInfo getFileUploadInfo() {
        return (IFileUploadInfo) getSession();
    }

    private LinkData createView(final ResourceData resourceData) {
        LinkData linkData = new LinkData();
        linkData.setId(resourceData.getId());
        linkData.setName(resourceData.getUrlDisplay());
        linkData.setTitle(resourceData.getUrlTitle());
        FileUploadTypeType type = FileUploadTypeType.getByMimeType(resourceData.getType());
        // TODO handle null here
        linkData.setResourceType(type);
        linkData.setHideLink(resourceData.getHideLink());
        linkData.setUrl(resourceData.getUrl());
        return linkData;
    }

}
