package com.madalla.webapp.cms.admin;

import java.io.File;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.service.cms.IContentAdminService;
import com.madalla.service.cms.IContentAdminServiceProvider;

public class RepositoryContentAdminPanel extends Panel {

	private static final long serialVersionUID = -218029530127246694L;

	public RepositoryContentAdminPanel(String name) {
		super(name);
		
		final FeedbackPanel backupMessages = new FeedbackPanel("backupMessages");
		backupMessages.setOutputMarkupId(true);
		add(backupMessages);
		
        add(new AjaxFallbackLink("backupLink"){
            
            public void onClick(AjaxRequestTarget target) {
            	target.addComponent(backupMessages);
            	String fileName;
            	try {
            		IContentAdminServiceProvider service = (IContentAdminServiceProvider)getPage().getApplication();
            		fileName = service.getContentAdminService().backupContentRoot();
                    info("Content Repository backed up to file. File name=" + fileName);
            	} catch (Exception e){
            	    error("Backup failed. "+ e.getLocalizedMessage());	
            	}
            }
            
        });
        
        //TODO popup to select backup file
        add(new AjaxFallbackLink("restoreLink"){
            public void onClick(AjaxRequestTarget target) {
                IContentAdminServiceProvider provider = (IContentAdminServiceProvider)getPage().getApplication();
                IContentAdminService service = provider.getContentAdminService();
                File[] backupFiles = service.getApplicationBackupFileList();
                if (backupFiles.length > 0){
                    service.restoreContentApplication(backupFiles[0]);
                }
            }
            
        });
        
        ContentDisplayPanel contentDisplayPanel = new ContentDisplayPanel("contentDisplayPanel");
        contentDisplayPanel.setOutputMarkupId(true);
        add(contentDisplayPanel);
        add(new ContentExplorerPanel("contentExplorerPanel", contentDisplayPanel, true));
	}

}
