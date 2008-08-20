package com.madalla.webapp.cms.admin;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.madalla.service.cms.IContentAdminService;
import com.madalla.service.cms.IContentAdminServiceProvider;

public class ContentAdminPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private Boolean adminApp;
	public File file ;
	
	public ContentAdminPanel(String name){
		this(name, false);
	}

	public ContentAdminPanel(String name, final Boolean adminApp) {
		super(name);
		this.adminApp = adminApp;
		
		final FeedbackPanel backupMessages = new FeedbackPanel("backupMessages");
		backupMessages.setOutputMarkupId(true);
		add(backupMessages);
	
        //Restore File List
        List<File> backupFiles = getContentAdminService().getBackupFileList();
        if (backupFiles.size() > 0){
        	Collections.sort(backupFiles);
        	Collections.reverse(backupFiles);
        	file = backupFiles.get(0);
        }
        final ListChoice listChoice = new ListChoice("backupFiles", new PropertyModel(this,"file") ,
        		backupFiles, new ChoiceRenderer("name"),10);
        listChoice.setOutputMarkupId(true);
        add(new RestoreForm("restoreForm", listChoice));
		
		add(new AjaxFallbackLink("backupLink"){
            
            public void onClick(AjaxRequestTarget target) {
            	target.addComponent(listChoice);
            	String fileName;
            	try {
            		IContentAdminService service = getContentAdminService();
            		
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
        Link rollback = new AjaxFallbackLink("rollbackLink"){

			private static final long serialVersionUID = -6873075723947980651L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				IContentAdminService service = getContentAdminService();
				service.rollbackSiteRestore();
			}
			
			@Override
			protected final void onBeforeRender(){
				IContentAdminService service = getContentAdminService();
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
        rollback.setOutputMarkupId(true);
        add(rollback);
        
        ContentDisplayPanel contentDisplayPanel = new ContentDisplayPanel("contentDisplayPanel");
        contentDisplayPanel.setOutputMarkupId(true);
        add(contentDisplayPanel);
        add(new ContentExplorerPanel("contentExplorerPanel", contentDisplayPanel, adminApp));

	}
	
	public class RestoreForm extends Form{

		private static final long serialVersionUID = 4729370802782788350L;
		
		
		public RestoreForm(String id, ListChoice listChoice){
			super(id);
	        add(listChoice);
		}

		@Override
		protected void onSubmit() {
			if (file != null ){
				if (adminApp){
					getContentAdminService().restoreContentApplication(file);
					info("Content Repository restored from file");
				} else {
					getContentAdminService().restoreContentSite(file);
					info("Site Content Data restored from file");
				}
			}
			super.onSubmit();
		}

		@Override
		protected void validate() {
			if (null == file){
				error("You must select a File to restore");
			}
			super.validate();
		}

	}
	
	public IContentAdminService getContentAdminService(){
		IContentAdminServiceProvider provider = (IContentAdminServiceProvider)getApplication();
		return provider.getContentAdminService();
	}
	
}
