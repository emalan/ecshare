package com.madalla.db.dao;

import org.joda.time.DateTime;

import com.madalla.bo.email.EmailEntryData;
import com.madalla.bo.email.IEmailEntryData;

public class EmailEntry extends EmailEntryData implements IEmailEntryData {

	private static final long serialVersionUID = 1L;

	private int id;
	private DateTime dateTime;
	private String senderComment;
	private String senderName;
	private String senderEmailAddress;
	
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

	public String getSenderName() {
		return senderName;
	}

	public String getSenderEmailAddress() {
		return senderEmailAddress;
	}

	public String getSenderComment() {
		return senderComment;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;		
	}

	public void setSenderComment(String senderComment) {
		this.senderComment = senderComment;		
	}

	public void setSenderEmailAddress(String senderEmailAddress) {
		this.senderEmailAddress = senderEmailAddress;		
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

}
