package com.madalla.db.dao.springjdbc;

import org.emalan.cms.bo.email.EmailEntryData;
import org.emalan.cms.bo.email.IEmailEntryData;
import org.joda.time.DateTime;

public class EmailEntry extends EmailEntryData implements IEmailEntryData {

	private static final long serialVersionUID = 1L;

	private long id;
	private DateTime dateTime;
	private String senderComment;
	private String senderName;
	private String senderEmailAddress;

    @Override
    public String getIdAsString() {
        return String.valueOf(id);
    }
    
	@Override
	public Long getId() {
		return id;
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
