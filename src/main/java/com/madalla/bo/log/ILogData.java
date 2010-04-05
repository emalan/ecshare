package com.madalla.bo.log;

import org.joda.time.DateTime;

public interface ILogData {

	DateTime getDateTime();

	void setDateTime(DateTime dateTime);

	String getUser();

	void setUser(String user);

	String getType();

	void setType(String type);

	String getCmsId();

	void setCmsId(String cmsId);
}
