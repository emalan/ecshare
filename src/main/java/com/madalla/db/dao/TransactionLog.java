package com.madalla.db.dao;

import org.joda.time.DateTime;

import com.madalla.bo.log.LogData;

public class TransactionLog extends LogData {
	private static final long serialVersionUID = 1093928382060262533L;

	private int id;
	private DateTime dateTime;
	private String user;
	private String type;
	private String cmsId;
	
	@Override
	public String getId() {
		return String.valueOf(id);
	}

	public void setId(int id){
		this.id = id;
	}
	
	@Override
	public String getName() {
		return dateTime.toString("yyyy-MM-dd'T'HHmmssSSS");
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
