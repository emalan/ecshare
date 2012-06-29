package com.madalla.db.dao;

import org.apache.commons.lang.StringUtils;
import org.emalan.cms.bo.log.LogData;
import org.joda.time.DateTime;

public class TransactionLog extends LogData{

    private static final long serialVersionUID = 1L;
    
    private long key;
    private String site;
    private DateTime dateTime;
    private String user;
    private String type;
    private String cmsId;

    public String getId() {
        return StringUtils.defaultIfEmpty("", String.valueOf(key));
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long id){
        this.key = id;
    }

    @Override
    public String getName() {
        return dateTime.toString("yyyy-MM-dd'T'HHmmssSSS");
    }
    
    @Override
    public String getSite() {
        return site;
    }

    @Override
    public void setSite(final String site) {
        this.site = site;
    }
    
    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCmsId() {
        return cmsId;
    }

    public void setCmsId(String cmsId) {
        this.cmsId = cmsId;
    }



}
