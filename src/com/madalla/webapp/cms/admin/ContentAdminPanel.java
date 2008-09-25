package com.madalla.webapp.cms.admin;

import static com.madalla.webapp.PageParams.RETURN_PAGE;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.PageParameters;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import com.madalla.service.cms.IRepositoryAdminService;
import com.madalla.service.cms.IRepositoryAdminServiceProvider;

public class ContentAdminPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private Boolean adminApp;
	public File file ;
	public List<File> backupFiles ;
	
	public static ContentAdminPanel newInstance(String name, final PageParameters params){
	    return new ContentAdminPanel(name, false, params);	
	}
	public static ContentAdminPanel newAdminInstance(String name, final PageParameters params){
		return new ContentAdminPanel(name, true, params);
	}

	private ContentAdminPanel(String name, final Boolean adminApp, final PageParameters params) {
		super(name);
		this.adminApp = adminApp;
		
		Class<? extends Page> returnPage ;
		try {
			String pageString = params.getString(RETURN_PAGE);
			returnPage = (Class<? extends Page>) Class.forName(pageString);
        } catch (Exception e) {
        	returnPage = getApplication().getHomePage();
        }
		add(new PageLink("returnLink", returnPage));
		
		final FeedbackPanel backupMessages = new FeedbackPanel("backupMessages");
		backupMessages.setOutputMarkupId(true);
		add(backupMessages);
	
        setBackupFileList();
        
        final ListChoice listChoice = new ListChoice("backupFiles", new PropertyModel(this,"file"), 
        		new PropertyModel(this,"backupFiles"), new ChoiceRenderer("name"), 8){

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
        add(new RestoreForm("restoreForm", listChoice));
		
		add(new AjaxFallbackLink("backupLink"){
            
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
        Link rollback = new AjaxFallbackLink("rollbackLink"){

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
        	Collections.reverse(backupFiles);
        	if (file == null){
        		file = backupFiles.get(0);
        	}
        	return true;
        }
        return false;
	}
	
}
