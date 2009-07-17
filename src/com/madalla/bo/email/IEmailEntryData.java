package com.madalla.bo.email;

import org.joda.time.DateTime;

public interface IEmailEntryData {

	String getSenderName() ;
	void setSenderName(String senderName) ;
	String getSenderEmailAddress() ;
	void setSenderEmailAddress(String senderEmailAddress) ;
	String getSenderComment() ;
	void setSenderComment(String senderComment) ;
	DateTime getDateTime() ;
	void setDateTime(DateTime dateTime);

}