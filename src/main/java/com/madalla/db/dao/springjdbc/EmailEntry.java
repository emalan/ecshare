package com.madalla.db.dao.springjdbc;

import org.apache.commons.lang.StringUtils;
import org.emalan.cms.bo.email.EmailEntryData;
import org.joda.time.DateTime;

public class EmailEntry extends EmailEntryData {

	private static final long serialVersionUID = 1L;

	private Long key ;
	private DateTime dateTime;
	private String senderComment;
	private String senderName;
	private String senderEmailAddress;

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
