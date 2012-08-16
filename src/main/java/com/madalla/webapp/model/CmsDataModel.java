package com.madalla.webapp.model;

import org.apache.wicket.model.IModel;
import org.emalan.cms.IDataService;
import org.emalan.cms.ISessionDataService;
import org.emalan.cms.bo.AbstractData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CmsDataModel implements IModel<AbstractData>{

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(CmsDataModel.class);
    
    private final IModel<AbstractData> model;
    private transient final IDataService service;
    private transient final ISessionDataService session;

    public CmsDataModel(final IModel<AbstractData> model, final IDataService service, final ISessionDataService session) {
        this.model = model;
        this.service = service;
        this.session = session;
    }
    
    @Override
    public void detach() {
        model.detach();
    }

    @Override
    public AbstractData getObject() {
        return model.getObject();
    }

    @Override
    public void setObject(AbstractData data) {
        model.setObject(data);
        log.info("saveData - " + data);
        session.validateTransaction(data);
        session.logTransaction(data);
        service.saveDataObject(data, session.getUser().getName());
        
    }

}
