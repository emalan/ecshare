package com.madalla.cms.bo.impl.ocm.email;

import org.apache.commons.lang.StringUtils;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;
import org.joda.time.DateTime;

import com.madalla.bo.email.EmailEntryData;
import com.madalla.bo.email.IEmailData;

@Node
public class EmailEntry extends EmailEntryData {

	private static final long serialVersionUID = 1L;
	
	@Field(path=true) private String id;
	@Field private String senderName;
	@Field private String senderEmailAddress;
	@Field private String senderComment;
	@Field private DateTime dateTime;
	
	public EmailEntry(){
		
	}
	
	public EmailEntry(final IEmailData parent, final String dateTime){
		this.id = parent.getId() + "/" + dateTime;
	}
	
	@Override
	public String getName() {
		return StringUtils.substringAfterLast(getId(), "/");
	}
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getSenderEmailAddress() {
		return senderEmailAddress;
	}
	public void setSenderEmailAddress(String senderEmailAddress) {
		this.senderEmailAddress = senderEmailAddress;
	}
	public String getSenderComment() {
		return senderComment;
	}
	public void setSenderComment(String senderComment) {
		this.senderComment = senderComment;
	}

	public DateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}


}
