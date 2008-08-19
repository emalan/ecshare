package com.madalla.webapp.cms.admin;

import java.io.File;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import com.madalla.service.cms.IContentAdminService;
import com.madalla.service.cms.IContentAdminServiceProvider;

public class ContentAdminPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private Boolean adminApp;
	
	public ContentAdminPanel(String name){
		this(name, false);
	}

	public ContentAdminPanel(String name, final Boolean adminApp) {
		super(name);
		this.adminApp = adminApp;
		
		final FeedbackPanel backupMessages = new FeedbackPanel("backupMessages");
		backupMessages.setOutputMarkupId(true);
		add(backupMessages);
	
        add(new AjaxFallbackLink("backupLink"){
            
            public void onClick(AjaxRequestTarget target) {
            	target.addComponent(backupMessages);
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
        
        add(new RestoreForm("restoreForm"));
        
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
				if (service.isRollBackAvailable()){
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
		public File file ;
		
		public RestoreForm(String id){
			super(id);
			
	        //Restore File List
	        List<File> backupFiles = getContentAdminService().getBackupFileList();
	        file = backupFiles.get(0);
	        final ListChoice listChoice = new ListChoice("backupFiles", new Model(file) ,
	        		backupFiles, new ChoiceRenderer("name"),10){

						@Override
						protected void onSelectionChanged(Object newSelection) {
							// TODO Auto-generated method stub
							super.onSelectionChanged(newSelection);
						}

	        	
	        };
	        add(listChoice);

		}

		@Override
		protected void onSubmit() {
			if (file != null ){
				if (adminApp){
					getContentAdminService().restoreContentApplication(file);
				} else {
					getContentAdminService().restoreContentSite(file);
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
