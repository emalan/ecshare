package com.madalla.webapp.cms.admin;

import java.io.File;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import com.madalla.service.cms.IContentAdminService;
import com.madalla.service.cms.IContentAdminServiceProvider;

public class ContentAdminPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public ContentAdminPanel(String name) {
		super(name);
		
        add(new AjaxFallbackLink("backupLink"){
            
            public void onClick(AjaxRequestTarget target) {
                IContentAdminServiceProvider service = (IContentAdminServiceProvider)getPage().getApplication();
                service.getContentAdminService().backupContentSite();
            }
            
        });
        
        add(new RestoreForm("restoreForm"));
        
        
//        add(new AjaxFallbackLink("restoreLink"){
//            public void onClick(AjaxRequestTarget target) {
//            	target.addComponent(listChoice);
//            	listChoice.setEnabled(true);
//            }
//            
//        });
        
        //Rollback link
        Link rollback = new AjaxFallbackLink("rollbackLink"){

			private static final long serialVersionUID = -6873075723947980651L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				IContentAdminServiceProvider provider = (IContentAdminServiceProvider)getPage().getApplication();
				IContentAdminService service = provider.getContentAdminService();
				service.rollbackSiteRestore();
			}
			
			@Override
			protected final void onBeforeRender(){
				IContentAdminServiceProvider provider = (IContentAdminServiceProvider)getPage().getApplication();
				IContentAdminService service = provider.getContentAdminService();
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
        add(new ContentExplorerPanel("contentExplorerPanel", contentDisplayPanel));

	}
	
	public class RestoreForm extends Form{

		private static final long serialVersionUID = 4729370802782788350L;
		private File file;
		
		public RestoreForm(String id){
			super(id);
			
	        //Restore File List
	        IContentAdminServiceProvider provider = (IContentAdminServiceProvider)getPage().getApplication();
	        IContentAdminService service = provider.getContentAdminService();
	        List<File> backupFiles = service.getBackupFileList();
	        
	        final ListChoice listChoice = new ListChoice("backupFiles", new Model(file) ,
	        		backupFiles, new ChoiceRenderer("name"));
	        add(listChoice);

		}

		@Override
		protected void onSubmit() {
			IContentAdminServiceProvider provider = (IContentAdminServiceProvider)getPage().getApplication();
			IContentAdminService service = provider.getContentAdminService();
			service.restoreContentSite(file);
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
	
}
