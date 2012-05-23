package com.madalla.webapp.admin.content;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authroles.authorization.strategies.role.metadata.MetaDataRoleAuthorizationStrategy;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.emalan.cms.BackupFile;
import org.emalan.cms.IRepositoryAdminService;
import org.emalan.cms.jcr.model.ContentNode;
import org.emalan.cms.jcr.model.IContentNode;
import org.emalan.cms.jcr.model.tree.AbstractTreeNode;

import com.madalla.service.ApplicationService;
import com.madalla.service.IApplicationServiceProvider;
import com.madalla.webapp.admin.pages.AdminPanelLink;
import com.madalla.webapp.css.Css;
import com.madalla.wicket.IndicatingAjaxSubmitLink;

public class ContentAdminPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private Boolean adminApp;
	public BackupFile file ;
	public List<BackupFile> backupFiles ;

	private ContentDisplayPanel contentDisplayPanel;
	private ContentExplorerPanel contentExplorerPanel;

	public static ContentAdminPanel newAdminInstance(String name){
		return new ContentAdminPanel(name, true);
	}

	public ContentAdminPanel(String name){
		this(name, false);
	}

	private ContentAdminPanel(String name, final Boolean adminApp) {
		super(name);
		this.adminApp = adminApp;

		Component adminPanelLink;
		add(adminPanelLink = new AdminPanelLink("AllSitesLink", ContentAdminPanel.class){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick() {
				Panel panel = ContentAdminPanel.newAdminInstance(ID);
				getPage().replace(panel);
			}

			@Override
			public boolean isEnabled() {
				return !adminApp;
			}
		});
		MetaDataRoleAuthorizationStrategy.authorize(adminPanelLink, RENDER, "SUPERADMIN");

		Component adminPanelLinkSingle;
		add(adminPanelLinkSingle = new AdminPanelLink("SingleSiteLink", ContentAdminPanel.class){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isEnabled() {
				return adminApp;
			}
		});
		MetaDataRoleAuthorizationStrategy.authorize(adminPanelLinkSingle, RENDER, "SUPERADMIN");

        setBackupFileList();
        final ListChoice<BackupFile> listChoice = new ListChoice<BackupFile>("backupFiles", new PropertyModel<BackupFile>(this,"file"),
        		 new PropertyModel<List<? extends BackupFile>>(this,"backupFiles"),new ChoiceRenderer<BackupFile>("displayName"), 8){
					private static final long serialVersionUID = 1L;

					@Override
					protected void onBeforeRender() {
						if (setBackupFileList()){
							setEnabled(true);
						} else {
							setEnabled(false);
						}
						super.onBeforeRender();
					}
        };
        listChoice.setOutputMarkupId(true);
        final Form<Object> form = new RestoreForm("restoreForm", listChoice);
        add(form);

        final FeedbackPanel backupMessages = new FeedbackPanel("backupMessages",new IFeedbackMessageFilter(){
			private static final long serialVersionUID = 1L;

			public boolean accept(FeedbackMessage message) {
				if (message.getReporter().getId().equals("backupLink")){
					return true;
				} else {
				    return false;
				}
			}
        });
		backupMessages.setOutputMarkupId(true);
		add(backupMessages);

		//Backup link
        AjaxLink<Object> backupLink = new IndicatingAjaxLink<Object>("backupLink"){
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
            	target.add(listChoice);
            	try {
            		IRepositoryAdminService service = getContentAdminService();
            		if (adminApp){
            			service.backupContentRoot();
            		} else {
            			service.backupContentSite();
            		}
                    info("Content Repository backed up successfully");
                    target.add(backupMessages);
            	} catch (Exception e){
            	    error("Backup failed. "+ e.getLocalizedMessage());
            	}
            }
        };
        add(backupLink);

		final FeedbackPanel restoreMessages = new FeedbackPanel("restoreMessages", new IFeedbackMessageFilter(){
			private static final long serialVersionUID = 1L;

			public boolean accept(FeedbackMessage message) {
				if (message.getReporter().getId().equals("rollbackLink")||
						message.getReporter().getId().equals("submitLink")){
					return true;
				} else {
					return false;
				}
			}
		});
		restoreMessages.setOutputMarkupId(true);
		add(restoreMessages);

        //Rollback link
        final AjaxLink<Object> rollBackLink = new IndicatingAjaxLink<Object>("rollbackLink"){
			private static final long serialVersionUID = -6873075723947980651L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				IRepositoryAdminService service = getContentAdminService();
				if (adminApp){
					service.rollbackApplicationRestore();
				} else {
					service.rollbackSiteRestore();
				}
				info("Rolled back to before Restore.");
				target.add(restoreMessages);
			}

			@Override
			protected final void onBeforeRender(){
				IRepositoryAdminService service = getContentAdminService();
				if (adminApp && service.isRollbackApplicationAvailable()){
					setEnabled(true);
				} else if (!adminApp && service.isRollbackSiteAvailable()){
					setEnabled(true);
				} else {
					setEnabled(false);
				}
                super.onBeforeRender();
            }
        };
        rollBackLink.setOutputMarkupId(true);
        add(rollBackLink);

        add(new IndicatingAjaxSubmitLink("submitLink", form){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				restoreContent();
				info("Restore successful.");
				target.add(restoreMessages);
				target.add(rollBackLink);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				warn("Restore failed!");
			}
        });

        contentDisplayPanel = new ContentDisplayPanel("contentDisplayPanel", this);
        contentDisplayPanel.setOutputMarkupId(true);
        add(contentDisplayPanel);

        contentExplorerPanel = new ContentExplorerPanel("contentExplorerPanel", this, adminApp){
			private static final long serialVersionUID = 1L;

			@Override
			public void onNodeClicked(AbstractTreeNode currentNode, AjaxRequestTarget target) {
				if (currentNode.getObject() instanceof ContentNode) {
                    IContentNode contentNode = currentNode.getObject();
                    String path = contentNode.getPath();
                    contentDisplayPanel.refresh(path);
                    target.add(contentDisplayPanel);
                }

			}

        };
        contentExplorerPanel.setOutputMarkupId(true);
        add(contentExplorerPanel);

	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.renderCSSReference(Css.CSS_FORM);
	}

	public class RestoreForm extends Form<Object>{

		private static final long serialVersionUID = 4729370802782788350L;


		public RestoreForm(String id, final ListChoice<BackupFile> listChoice){
			super(id);
	        add(listChoice);
		}



		@Override
		protected void onSubmit() {
			if (!isSubmitted()) {
				restoreContent();
			}
		}

		@Override
		protected void onValidate() {
			if (null == file){
				error("You must select a File to restore");
			}
		}

	}

	private void restoreContent(){
		if (file != null ){
			if (adminApp){
				getContentAdminService().restoreContentApplication(file.getFile());
				info("Content Repository restored from file");
			} else {
				getContentAdminService().restoreContentSite(file.getFile());
				info("Site Content Data restored from file");
			}
		}
	}
	
    private ApplicationService getApplicationService() {
        return ((IApplicationServiceProvider) getApplication()).getApplicationService();
    }

	public IRepositoryAdminService getContentAdminService(){
	    return getApplicationService().getRepositoryAdminService();
	}

	public Boolean setBackupFileList(){
		if (adminApp){
			backupFiles = getContentAdminService().getApplicationBackupFileList();
		} else {
			backupFiles = getContentAdminService().getBackupFileList();
		}
        if (backupFiles.size() > 0){
        	Collections.sort(backupFiles);
        	//Collections.reverse(backupFiles);
        	if (file == null){
        		file = backupFiles.get(0);
        	}
        	return true;
        }
        return false;
	}

	public Component getDisplayPanel(){
		return get("contentDisplayPanel");
	}

	public Component getExplorerPanel(){
		return get("contentExplorerPanel");
	}

	boolean isCopyable(){
		return contentExplorerPanel.isCopyable();
	}

	boolean isPasteable(){
		return contentExplorerPanel.isPasteable();
	}

	void copyNode(){
		contentExplorerPanel.copyNode();
	}

	void pasteNode(AjaxRequestTarget target){
		contentExplorerPanel.pasteNode(target);
	}

	void deleteNode(AjaxRequestTarget target){
		contentExplorerPanel.deleteCurrentNode(target);
	}

	void refreshDisplayPanel(String path){
		contentDisplayPanel.refresh(path);
	}
}
