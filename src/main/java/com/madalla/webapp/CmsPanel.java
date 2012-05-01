package com.madalla.webapp;

import org.apache.wicket.markup.html.panel.Panel;
import org.emalan.cms.IDataService;
import org.emalan.cms.IDataServiceProvider;
import org.emalan.cms.ISessionDataService;
import org.emalan.cms.bo.AbstractData;

import com.madalla.BuildInformation;
import com.madalla.service.ApplicationService;

public abstract class CmsPanel extends Panel {

	private static final long serialVersionUID = 1L;

	protected static final String APPNODE = "AppPage";

	public CmsPanel(String id) {
		super(id);
	}

	protected CmsApplication getCmsApplication(){
		return (CmsApplication) getApplication();
	}
	
	protected ApplicationService getApplicationService() {
		return  ((CmsApplication)getApplication()).getApplicationService();
	}

	protected BuildInformation getBuildInfo(){
		return getApplicationService().getBuildInformation();
	}
	
	protected IDataService getRepositoryService(){
		return ((IDataServiceProvider)getApplication()).getRepositoryService();
	}

	protected void saveData(AbstractData data){
		getSessionDataService().validateTransaction(data);
		getSessionDataService().logTransaction(data);
		IDataService service = ((IDataServiceProvider)getApplication()).getRepositoryService();
		service.saveDataObject(data, getSessionDataService().getUser().getName());
	}

	protected ISessionDataService getSessionDataService(){
		return getAppSession().getRepositoryService();
	}

	protected CmsSession getAppSession(){
        return (CmsSession)getSession();
    }




}
