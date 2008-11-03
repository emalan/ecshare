package com.madalla.webapp.cms.admin;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.madalla.service.cms.BackupFile;
import com.madalla.service.cms.IRepositoryAdminService;
import com.madalla.service.cms.IRepositoryAdminServiceProvider;
import com.madalla.wicket.IndicatingAjaxSubmitLink;

public class ContentAdminPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private Boolean adminApp;
	public BackupFile file ;
	public List<BackupFile> backupFiles ;
	
	private ContentDisplayPanel contentDisplayPanel;
	private ContentExplorerPanel contentExplorerPanel;
	
	public static ContentAdminPanel newInstance(String name, Class<? extends Page> returnPage){
	    return new ContentAdminPanel(name, false, returnPage);	
	}
	public static ContentAdminPanel newAdminInstance(String name, Class<? extends Page> returnPage){
		return new ContentAdminPanel(name, true, returnPage);
	}

	private ContentAdminPanel(String name, final Boolean adminApp, Class<? extends Page> returnPage) {
		super(name);
		this.adminApp = adminApp;
		
        Link link = new PageLink("returnLink", returnPage);
        if (adminApp){
        	link.setVisible(false);
        }
        add(link);
		
		final FeedbackPanel backupMessages = new FeedbackPanel("backupMessages");
		backupMessages.setOutputMarkupId(true);
		add(backupMessages);
	
        setBackupFileList();
        final ListChoice listChoice = new ListChoice("backupFiles", new PropertyModel(this,"file"), 
        		new PropertyModel(this,"backupFiles"),new ChoiceRenderer("displayName"), 8){ 
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
        final Form form = new RestoreForm("restoreForm", listChoice);
        add(form);
		
        //Backup link
		add(new IndicatingAjaxLink("backupLink"){
			private static final long serialVersionUID = 1L;

			public void onClick(AjaxRequestTarget target) {
            	target.addComponent(listChoice);
            	String fileName;
            	try {
            		IRepositoryAdminService service = getContentAdminService();
            		
            		if (adminApp){
            			fileName = service.backupContentRoot();
            		} else {
            			fileName = service.backupContentSite();
            		}
                    info("Content Repository backed up to file. File name=" + fileName);
            	} catch (Exception e){
            	    error("Backup failed. "+ e.getLocalizedMessage());	
            	}
            }
        });

        //Rollback link
        add(new IndicatingAjaxLink("rollbackLink"){
			private static final long serialVersionUID = -6873075723947980651L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				IRepositoryAdminService service = getContentAdminService();
				if (adminApp){
					service.rollbackApplicationRestore();
				} else {
					service.rollbackSiteRestore();
				}
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
        });
        
        add(new IndicatingAjaxSubmitLink("submitLink", form){
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form form) {
				restoreContent();
			}
        });
        
        contentDisplayPanel = new ContentDisplayPanel("contentDisplayPanel", this);
        contentDisplayPanel.setOutputMarkupId(true);
        add(contentDisplayPanel);
        
        contentExplorerPanel = new ContentExplorerPanel("contentExplorerPanel", this, adminApp);
        contentExplorerPanel.setOutputMarkupId(true);
        add(contentExplorerPanel);

	}
	
	public class RestoreForm extends Form{

		private static final long serialVersionUID = 4729370802782788350L;
		
		
		public RestoreForm(String id, final ListChoice listChoice){
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
		protected void validate() {
			if (null == file){
				error("You must select a File to restore");
			}
			super.validate();
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
	
	public IRepositoryAdminService getContentAdminService(){
		IRepositoryAdminServiceProvider provider = (IRepositoryAdminServiceProvider)getApplication();
		return provider.getRepositoryAdminService();
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
	
	public void refreshExplorerPanel(){
		contentExplorerPanel.refresh();
	}
	
	public void refreshDisplayPanel(String path){
		contentDisplayPanel.refresh(path);
	}
	
}
