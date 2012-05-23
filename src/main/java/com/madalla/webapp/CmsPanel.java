package com.madalla.webapp;

import org.apache.wicket.markup.html.panel.Panel;
import org.emalan.cms.IDataService;
import org.emalan.cms.IDataServiceProvider;
import org.emalan.cms.ISessionDataService;
import org.emalan.cms.bo.AbstractData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.madalla.BuildInformation;
import com.madalla.service.ApplicationService;
import com.madalla.service.IApplicationServiceProvider;

public abstract class CmsPanel extends Panel {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(CmsPanel.class);

	protected static final String APPNODE = "AppPage";

	public CmsPanel(String id) {
		super(id);
	}

	protected CmsApplication getCmsApplication(){
		return (CmsApplication) getApplication();
	}
	
	protected ApplicationService getApplicationService() {
		return  ((IApplicationServiceProvider)getApplication()).getApplicationService();
	}

	protected BuildInformation getBuildInfo(){
		return getApplicationService().getBuildInformation();
	}
	
	protected IDataService getRepositoryService(){
		return getApplicationService().getRepositoryService();
	}

	protected void saveData(AbstractData data){
		log.info("daveData - " + data);
		getSessionDataService().validateTransaction(data);
		getSessionDataService().logTransaction(data);
		IDataService service = getApplicationService().getRepositoryService();
		service.saveDataObject(data, getSessionDataService().getUser().getName());
	}

	protected ISessionDataService getSessionDataService(){
		return getAppSession().getRepositoryService();
	}

	protected CmsSession getAppSession(){
        return (CmsSession)getSession();
    }




}
