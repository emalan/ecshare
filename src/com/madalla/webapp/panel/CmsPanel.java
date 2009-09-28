package com.madalla.webapp.panel;

import org.apache.wicket.markup.html.panel.Panel;

import com.madalla.bo.AbstractData;
import com.madalla.service.IDataService;
import com.madalla.service.IDataServiceProvider;
import com.madalla.service.ISessionDataService;
import com.madalla.service.ISessionDataServiceProvider;

public abstract class CmsPanel extends Panel {

	private static final long serialVersionUID = 1L;

	public CmsPanel(String id) {
		super(id);
	}
	
	protected IDataService getRepositoryService(){
		return ((IDataServiceProvider)getApplication()).getRepositoryService();
	}
	
	protected void saveData(AbstractData data){
		getSessionDataService().validateTransaction(data);
		IDataService service = ((IDataServiceProvider)getApplication()).getRepositoryService();
		service.saveDataObject(data);
	}
	
	private ISessionDataService getSessionDataService(){
		return ((ISessionDataServiceProvider) getSession()).getRepositoryService();
	}
	



}
